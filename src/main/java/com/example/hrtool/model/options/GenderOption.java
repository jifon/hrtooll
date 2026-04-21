package com.example.hrtool.model.options;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenderOption {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String value;

    @Column(nullable = true)
    boolean isActive = true;

    @PrePersist
    public void prePersist() {
        if (!this.isActive) {
            this.isActive = true;
        }
    }
}

