package com.example.hrtool.dto;

import com.example.hrtool.model.Inventory;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class HardwareItemDto {
    private Long id;
    private String name;
    private String inventoryNr;
    private Long inventoryId;
}
