package com.example.hrtool.service.impl;


import com.example.hrtool.dto.AccessDataDto;
import com.example.hrtool.exception.ModelNotFoundException;
import com.example.hrtool.mapper.MappingUtils;
import com.example.hrtool.model.AccessData;
import com.example.hrtool.model.ContractInfo;
import com.example.hrtool.repository.AccessDataRepository;
import com.example.hrtool.repository.EmployeeProfileRepository;
import com.example.hrtool.service.AccessDataService;
import com.example.hrtool.service.EmployeeProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccessDataServiceImpl implements AccessDataService {

    private final AccessDataRepository repository;
    private final MappingUtils mappingUtils;
    private final EmployeeProfileRepository employeeProfileRepository;

    @Override
    public List<AccessDataDto> getAll() {
        return repository.findAll()
                .stream()
                .map(mappingUtils::mapToAccessDataDto)
                .collect(Collectors.toList());
    }

    @Override
    public AccessDataDto create(AccessDataDto dto) {
        return mappingUtils.mapToAccessDataDto(repository.save(mappingUtils.mapToAccessData(dto)));
    }

    @Override
    public AccessDataDto update(Long id, AccessDataDto dto) {
//        AccessData entity = mappingUtils.mapToAccessData(dto);
//        entity.setId(id);
//        return mappingUtils.mapToAccessDataDto(repository.save(entity));

        AccessData entity = repository.findById(id)
                .orElseThrow(() -> new ModelNotFoundException("AccessData not found"));

        if (dto.getBusinessPhone() != null) entity.setBusinessPhone(dto.getBusinessPhone());
        if (dto.getNameAbbreviation() != null) entity.setNameAbbreviation(dto.getNameAbbreviation());
        if (dto.getPersonnelNumber() != null) entity.setPersonnelNumber(dto.getPersonnelNumber());
        if (dto.getSapNumber() != null) entity.setSapNumber(dto.getSapNumber());
        if (dto.getEmail() != null) entity.setEmail(dto.getEmail());

        return mappingUtils.mapToAccessDataDto(repository.save(entity));

    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public AccessDataDto findByEmployeeId(Long id) {
        return repository.findAccessDataByEmployeeId(id)
                .map(mappingUtils::mapToAccessDataDto)
                .orElseGet(() -> {
                    // создать новый AccessData, привязать к employee
                    AccessData newAccess = new AccessData();
                    newAccess.setEmployee(employeeProfileRepository.findEmployeeProfileById(id)
                            .orElseThrow(() -> new ModelNotFoundException("Employee not found")));

                    // сохранить и вернуть DTO
                    return mappingUtils.mapToAccessDataDto(repository.save(newAccess));
                });
    }
}

