package com.example.hrtool.service.impl;


import com.example.hrtool.client.RabbitMQClient;
import com.example.hrtool.dto.AuthRequest;
import com.example.hrtool.dto.InternalAuthResponse;
import com.example.hrtool.dto.RegisterRequest;
import com.example.hrtool.dto.RegisterRequestWithId;
import com.example.hrtool.model.OutboxEvent;
import com.example.hrtool.model.SystemUser;
import com.example.hrtool.model.enums.OutboxEventType;
import com.example.hrtool.repository.OutboxEventRepository;
import com.example.hrtool.repository.SystemUserRepository;
import com.example.hrtool.security.JwtService;
import com.example.hrtool.service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SystemUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper mapper;

    private final RabbitMQClient rabbitMQClient;

    private final SystemUserRepository systemUserRepository;
    private final OutboxEventRepository outboxEventRepository;

    @Override
    public InternalAuthResponse register(RegisterRequest request){
        SystemUser user = new SystemUser();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setRole(request.getRole());
        user.setActive(true);

        RegisterRequestWithId requestWithId = new RegisterRequestWithId();
        requestWithId.setEmail(request.getEmail());
        requestWithId.setFullName(request.getFullName());
        requestWithId.setRole(request.getRole());
        requestWithId.setPassword(passwordEncoder.encode(request.getPassword()));

        user = repository.save(user);

        String refreshToken = jwtService.generateRefreshToken(user.getId());
        Date expirationDate = new Date(System.currentTimeMillis() + 1000 * 60 * 15);
        String accessToken = jwtService.generateAccessToken(user, expirationDate);
        requestWithId.setId(user.getId());

        try {

            String message = mapper.writeValueAsString(requestWithId);
            OutboxEvent outboxEvent = new OutboxEvent();
            outboxEvent.setType(OutboxEventType.NEW_ACCOUNT);
            outboxEvent.setTimestamp(Instant.now());
            outboxEvent.setPayload(message);
            outboxEventRepository.save(outboxEvent);
            return new InternalAuthResponse(accessToken, refreshToken, user.getFullName(), user.getRole().toString() );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InternalAuthResponse login(AuthRequest request) {
        if(!systemUserRepository.existsBy()){
            RegisterRequest req = new RegisterRequest();
            req.setEmail(request.getEmail());
            req.setPassword(request.getPassword());
            req.setRole(SystemUser.Role.ADMIN);
            req.setFullName("PlatzhalterName");
            register(req);
        }

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        SystemUser user = repository.findByEmail(request.getEmail()).orElseThrow();
        String refreshToken = jwtService.generateRefreshToken(user.getId());
        Date expirationDate = new Date(System.currentTimeMillis() + 1000 * 60 * 15);
        String accessToken = jwtService.generateAccessToken(user, expirationDate);
        return new InternalAuthResponse(accessToken, refreshToken, user.getFullName(), user.getRole().toString());
    }

    @Override
    public String refresh(String refreshToken) {
        return jwtService.generateAccessToken(refreshToken);
    }

    @Override
    public boolean emailExists(String email) {
        return repository.findByEmail(email).isPresent();
    }

}
