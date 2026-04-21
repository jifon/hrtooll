package com.example.hrtool.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExternalAuthResponse {
    private String fullName;
    private String role;
    private String accessToken;
}

