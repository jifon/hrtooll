package com.example.hrtool.service;

import com.example.hrtool.model.SystemUser;

public interface ProcessAbortService {
    void startAbort(Long employeeId, SystemUser.Role initiatorRole);

    void confirmAbort(Long employeeId, SystemUser.Role role);
}

