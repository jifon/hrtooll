package com.example.hrtool.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_processed_employees",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "employee_id"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProcessedEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private SystemUser user;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private EmployeeProfile employee;

    private LocalDateTime processedAt;
}
