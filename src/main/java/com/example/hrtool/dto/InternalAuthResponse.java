package com.example.hrtool.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InternalAuthResponse {
    private String accessToken;
    private String refreshToken;
    private String fullName;
    private String role;
}

