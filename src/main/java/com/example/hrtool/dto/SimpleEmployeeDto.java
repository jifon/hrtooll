package com.example.hrtool.dto;


import lombok.Data;

@Data
public class SimpleEmployeeDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String departmentName;
}
