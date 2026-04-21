package com.example.hrtool.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePasswordDto {
    @NotEmpty
    @NotNull
    private String newPassword;

    @NotEmpty
    @NotNull
    private Long id;
}
