package com.example.hrtool.job;

import com.example.hrtool.client.RabbitMQClient;
import com.example.hrtool.model.OutboxEvent;
import com.example.hrtool.repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
@EnableScheduling
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.outbox.enabled", havingValue = "true", matchIfMissing = false)
public class OutboxPoller {

    private final OutboxEventRepository repository;

    private final RabbitMQClient rabbitMQClient;

    //checkt alle 15 Sekunden, ob neue Events vorhanden sind.
    @Scheduled(fixedDelay=15000)
    void checkForNewEvent(){

        List<OutboxEvent> events = repository.findAll();
        if(events.isEmpty()){
            return;
        }
        //Sicher gehen, dass die ersten Events auch als erstes abgearbeitet werden
        events.sort(Comparator.comparing(OutboxEvent::getTimestamp));
        for(OutboxEvent event : events){
            try {
                boolean success = handleEvent(event);
                if(success){
                    repository.delete(event);
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean handleEvent(OutboxEvent event) throws JsonProcessingException {
        switch(event.getType()){
            case NEW_ACCOUNT:
                rabbitMQClient.publishNewAccount(event.getPayload());
                break;
            case CHANGE_PASSWORD:
                rabbitMQClient.publishChangePassword(event.getPayload());
                break;
            default:
                return false;
        }
        return true;
    }
}
