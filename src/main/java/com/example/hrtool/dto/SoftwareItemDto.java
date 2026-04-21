package com.example.hrtool.dto;

import lombok.Data;

@Data
public class SoftwareItemDto {
    private Long id;
    private String name;
    private String version;   // Версия, например "2023"
    private String license;   // Лицензия, например "2"
    private String category;
    private Long inventoryId;
}
