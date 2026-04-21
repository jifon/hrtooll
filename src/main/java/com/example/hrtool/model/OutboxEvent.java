package com.example.hrtool.model;

import com.example.hrtool.model.enums.OutboxEventType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
public class OutboxEvent {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private OutboxEventType type;

    @Column(columnDefinition = "text")
    private String payload;

    private Instant timestamp;
}
