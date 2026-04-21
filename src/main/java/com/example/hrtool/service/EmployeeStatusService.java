package com.example.hrtool.service;

import com.example.hrtool.dto.EmployeeBlockStatusDto;
import com.example.hrtool.dto.SidebarSimpleEmployeeDto;
import com.example.hrtool.model.EmployeeProfile;

import java.util.List;

public interface EmployeeStatusService {

    List<EmployeeBlockStatusDto> getAllStatus();


    List<EmployeeBlockStatusDto> getFilteredStatus(Boolean personalData, Boolean companyData,
                                                   Boolean qualificationData, Boolean financeData,
                                                   Boolean inventoryData, Boolean accessData, Boolean sapNumber);

    List<SidebarSimpleEmployeeDto> getLastProcessedForMyRole();

    List<EmployeeBlockStatusDto> getStatusByRole(String role);

    List<EmployeeBlockStatusDto> getAdvancedFilteredStatus(String role, String name, Boolean personalData,
                                                           Boolean companyData, Boolean qualificationData,
                                                           Boolean financeData, Boolean inventoryData,
                                                           Boolean accessData, Boolean sapNumber);


//    void handleManualAdvance(Long id, String role);


    List<EmployeeBlockStatusDto> getArchived();

    EmployeeBlockStatusDto buildStatus(EmployeeProfile profile);


}