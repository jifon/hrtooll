package com.example.hrtool.service;

import com.example.hrtool.dto.ChangePasswordDto;
import com.example.hrtool.dto.SystemUserDto;
import com.example.hrtool.dto.UserNameEmailDto;

import java.util.List;
import java.util.Optional;

public interface SystemUserService {
    Optional<UserNameEmailDto> getUserNameAndEmail(Long id);

    // SystemUserService.java
    List<SystemUserDto> getAllUsers();

    SystemUserDto getCurrentUser(String username);

    SystemUserDto updateCurrentUser(String username, SystemUserDto dto);

    boolean updateOwnProfile(String email, SystemUserDto dto);

    void changePassword(ChangePasswordDto dto, Long id);
}

