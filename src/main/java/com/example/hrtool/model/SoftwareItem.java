package com.example.hrtool.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoftwareItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;      // Название, например "MS Visio"
    private String category;
    private String version;   // Версия, например "2023"
    private String license;   // Лицензия, например "2"

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;
}
