package com.example.hrtool.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubStatusOptionDto {
    private Long id;
    private String value;
    private boolean isActive;


}

