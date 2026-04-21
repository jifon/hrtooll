package com.example.hrtool.service;

import com.example.hrtool.dto.EmployeeBlockStatusDto;
import com.example.hrtool.model.EmployeeProfile;

import java.util.List;

public interface ProfileViewStatusService {

    void markAsViewed(Long employeeId);

    int getUnreadCount();

//    List<EmployeeProfile> getUnreadProfiles();
    List<EmployeeBlockStatusDto> getUnreadByRole();

    boolean isUnread(Long employeeId);
}
