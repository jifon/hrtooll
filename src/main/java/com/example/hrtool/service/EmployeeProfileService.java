package com.example.hrtool.service;

import com.example.hrtool.dto.EmployeeProfileDto;
import com.example.hrtool.dto.SimpleEmployeeDto;
import com.example.hrtool.model.EmployeeProfile;

import java.util.List;

public interface EmployeeProfileService {
    // Метод: пользователь начинает обрабатывать сотрудника
    void startProcessing(Long employeeId);

    List<SimpleEmployeeDto> getAllAbortedProfiles();

    List<EmployeeProfileDto> getAll();
    EmployeeProfileDto getById(Long id, String role);
    EmployeeProfileDto create(EmployeeProfileDto dto);
    EmployeeProfileDto update(Long id, EmployeeProfileDto dto);
    void delete(Long id);
    List<EmployeeProfileDto> filter(String status, String role, String name);

    Long createWithNameAndSurname(String firstName, String lastName);

}