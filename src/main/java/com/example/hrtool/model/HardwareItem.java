package com.example.hrtool.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"inventory"})
public class HardwareItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;        // Название оборудования, например "Laptop"
    private String inventoryNr; // Инвентарный номер, если есть

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;
}
