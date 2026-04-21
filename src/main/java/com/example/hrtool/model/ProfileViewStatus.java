package com.example.hrtool.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileViewStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private EmployeeProfile employee;

    @ManyToOne(optional = false)
    private SystemUser user;

    private boolean viewed = false;

    private LocalDateTime viewedAt;
}
