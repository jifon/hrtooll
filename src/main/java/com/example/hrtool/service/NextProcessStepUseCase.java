package com.example.hrtool.service;

import com.example.hrtool.dto.AdvanceResult;
import com.example.hrtool.model.SystemUser;
import org.springframework.aop.config.AdviceEntry;

public interface NextProcessStepUseCase {
    AdvanceResult proceed(Long employeeId, SystemUser.Role role);
}

