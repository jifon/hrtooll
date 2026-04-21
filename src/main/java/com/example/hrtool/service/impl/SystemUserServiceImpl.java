package com.example.hrtool.service.impl;

import com.example.hrtool.dto.ChangePasswordDto;
import com.example.hrtool.dto.SystemUserDto;
import com.example.hrtool.dto.UserNameEmailDto;
import com.example.hrtool.model.OutboxEvent;
import com.example.hrtool.model.SystemUser;
import com.example.hrtool.model.enums.OutboxEventType;
import com.example.hrtool.repository.OutboxEventRepository;
import com.example.hrtool.repository.SystemUserRepository;
import com.example.hrtool.service.SystemUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SystemUserServiceImpl implements SystemUserService {

    private final SystemUserRepository repository;
    private final OutboxEventRepository outboxEventRepository;

    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper mapper;

    @Override
    public Optional<UserNameEmailDto> getUserNameAndEmail(Long id) {
        return repository.findById(id)
                .map(user -> new UserNameEmailDto(user.getFullName(), user.getEmail()));
    }

    @Override
    public List<SystemUserDto> getAllUsers() {
        return repository.findAll().stream()
                .map(user -> new SystemUserDto(
                        user.getId(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getRole().name()
                ))
                .toList();
    }

    @Override
    public SystemUserDto getCurrentUser(String email) {
        SystemUser user = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        SystemUserDto dto = new SystemUserDto();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());

        return dto;
    }

    @Override
    public SystemUserDto updateCurrentUser(String email, SystemUserDto dto) {
        SystemUser user = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Обновляем поля
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        // можно добавить смену пароля с проверкой

        SystemUser updated = repository.save(user);

        SystemUserDto updatedDto = new SystemUserDto();
        updatedDto.setId(updated.getId());
        updatedDto.setFullName(updated.getFullName());
        updatedDto.setEmail(updated.getEmail());
        updatedDto.setRole(updated.getRole().name());

        return updatedDto;
    }

    public boolean updateOwnProfile(String email, SystemUserDto dto) {
        Optional<SystemUser> optionalUser = repository.findByEmail(email);
        if (optionalUser.isEmpty()) return false;

        SystemUser user = optionalUser.get();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());

        repository.save(user);
        return true;
    }

    @Override
    public void changePassword(ChangePasswordDto dto, Long id) {
        if(repository.findById(id).get().getRole() != SystemUser.Role.ADMIN){
            throw new RuntimeException("Nur Admins können Passwörter ändern!");
        }
        if(!repository.existsById(dto.getId())) {
            throw new RuntimeException("User not found");
        }
        SystemUser user = repository.findById(dto.getId()).get();
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        repository.save(user);
        dto.setNewPassword(passwordEncoder.encode(dto.getNewPassword()));
        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.setType(OutboxEventType.CHANGE_PASSWORD);
        outboxEvent.setTimestamp(Instant.now());
        try {
            outboxEvent.setPayload(mapper.writeValueAsString(dto));
            outboxEventRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
