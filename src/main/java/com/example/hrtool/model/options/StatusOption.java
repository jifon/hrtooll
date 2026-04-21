package com.example.hrtool.model.options;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusOption {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String value;

    @OneToMany(mappedBy = "parentStatus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubStatusOption> subStatuses;

    @Column(nullable = true)
    boolean isActive = true;

    @PrePersist
    public void prePersist() {
        if (!this.isActive) {
            this.isActive = true;
        }
    }
}
