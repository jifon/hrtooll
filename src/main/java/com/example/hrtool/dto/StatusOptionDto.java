package com.example.hrtool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusOptionDto {
    private Long id;
    private String value;
    private boolean isActive;
    private List<SubStatusOptionDto> subStatuses;

}

