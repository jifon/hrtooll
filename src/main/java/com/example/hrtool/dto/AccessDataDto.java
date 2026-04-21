package com.example.hrtool.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessDataDto {
    private Long id;
    private Long employeeId;
    private String nameAbbreviation;
    private String personnelNumber;
    private String sapNumber;
    private String email;
    private String businessPhone;

}
