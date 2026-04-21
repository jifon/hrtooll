package com.example.hrtool.client;

import com.example.hrtool.dto.RegisterRequestWithId;
import com.example.hrtool.repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQClient {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper mapper;

    private final OutboxEventRepository outboxEventRepository;

    @Value("${hrtool.rabbitmq.ExchangeName}")
    private String exchangeName;
    @Value("${hrtool.rabbitmq.RoutingKey.publishNewAccount}")
    private String publishNewAccRoutingKey;
    @Value("${hrtool.rabbitmq.RoutingKey.publishChangePassword}")
    private String publishChangePasswordRoutingKey;

    //this is called from the outboxEventPoller, which deletes the event itself, so we dont need to delete the event ourself
    public void publishNewAccount(String request){
        rabbitTemplate.convertAndSend(exchangeName, publishNewAccRoutingKey, request);
    }

    public void publishChangePassword(String payload){
        rabbitTemplate.convertAndSend(exchangeName, publishChangePasswordRoutingKey, payload);
    }
}
