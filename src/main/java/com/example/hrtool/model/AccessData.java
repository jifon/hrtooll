package com.example.hrtool.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    private String businessPhone;
    @OneToOne
    private EmployeeProfile employee;

    private String nameAbbreviation;
    private String personnelNumber;
    private String sapNumber;
    private String email;
}

