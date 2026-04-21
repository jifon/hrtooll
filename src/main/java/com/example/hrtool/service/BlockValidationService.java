package com.example.hrtool.service;

import com.example.hrtool.model.EmployeeProfile;
import com.example.hrtool.model.SystemUser;

public interface BlockValidationService {
    void validate(EmployeeProfile employee, SystemUser.Role role);
}
