package com.example.hrtool.model;

import com.example.hrtool.model.options.CommunicationTypeOption;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@ToString(exclude = {"hardwareItems", "softwareItems"})
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private EmployeeProfile employee;

//    @Enumerated(EnumType.STRING)
//    private CommunicationType communication;
    @ManyToOne
    private CommunicationTypeOption communicationType;

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HardwareItem> hardwareItems;

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SoftwareItem> softwareItems;
}

