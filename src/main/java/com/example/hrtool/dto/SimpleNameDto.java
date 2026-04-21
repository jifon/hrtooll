package com.example.hrtool.dto;

import lombok.Data;

@Data
public class SimpleNameDto {
    private Long id;
    private String name;
    private String category; // wird nur für SoftwareTool verwendet
}
