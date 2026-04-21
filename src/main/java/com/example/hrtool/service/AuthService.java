package com.example.hrtool.service;

import com.example.hrtool.dto.AuthRequest;
import com.example.hrtool.dto.InternalAuthResponse;
import com.example.hrtool.dto.RegisterRequest;

public interface AuthService {
    InternalAuthResponse register(RegisterRequest request);
    InternalAuthResponse login(AuthRequest request);
    String refresh(String refreshToken);
    boolean emailExists(String email);
}
