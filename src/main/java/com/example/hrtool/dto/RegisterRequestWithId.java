package com.example.hrtool.dto;

import com.example.hrtool.model.SystemUser;
import lombok.Data;

@Data
public class RegisterRequestWithId {
    private String email;
    private String password;
    private String fullName;
    private SystemUser.Role role;
    private Long id;
}
