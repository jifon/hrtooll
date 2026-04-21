package com.example.hrtool.dto;


import lombok.Data;

import java.util.List;

@Data
public class InventoryDto {
    private Long id;
    private Long employeeId;
    private Long communicationTypeId;
    private List<HardwareDto> hardware; // названия железа
    private List<SoftwareDto> software; // названия софта
}

