package com.example.hrtool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemUserDto {
    private Long id;
    private String fullName;
    private String email;
    private String role;

}
