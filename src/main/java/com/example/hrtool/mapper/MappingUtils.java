package com.example.hrtool.mapper;

import com.example.hrtool.dto.AccessDataDto;
import com.example.hrtool.exception.ModelNotFoundException;
import com.example.hrtool.model.AccessData;
import com.example.hrtool.repository.EmployeeProfileRepository;
import com.example.hrtool.service.EmployeeProfileService;
import org.springframework.stereotype.Service;

@Service
public class MappingUtils {

    private final EmployeeProfileRepository employeeProfileRepository;

    public MappingUtils(EmployeeProfileRepository employeeProfileRepository) {
        this.employeeProfileRepository = employeeProfileRepository;
    }

    public AccessDataDto mapToAccessDataDto(AccessData model) {
        AccessDataDto dto = new AccessDataDto();
        dto.setId(model.getId());
        dto.setEmployeeId(model.getEmployee() != null ? model.getEmployee().getId() : null);
        dto.setNameAbbreviation(model.getNameAbbreviation());
        dto.setPersonnelNumber(model.getPersonnelNumber());
        dto.setSapNumber(model.getSapNumber());
        dto.setEmail(model.getEmail());
        dto.setBusinessPhone(model.getBusinessPhone());
        return dto;
    }

    public AccessData mapToAccessData(AccessDataDto dto) {
        AccessData model = new AccessData();
        model.setId(dto.getId());
        model.setEmployee(dto.getEmployeeId() != null ? employeeProfileRepository.findEmployeeProfileById(dto.getEmployeeId()).orElseThrow(() -> new ModelNotFoundException("Employee by Id " + dto.getEmployeeId() + " was not found")) : null);
        model.setNameAbbreviation(dto.getNameAbbreviation());
        model.setPersonnelNumber(dto.getPersonnelNumber());
        model.setSapNumber(dto.getSapNumber());
        model.setEmail(dto.getEmail());
        model.setBusinessPhone(dto.getBusinessPhone());
        return model;
    }
}
