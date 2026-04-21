package com.example.hrtool.service;

import com.example.hrtool.dto.ContractInfoDto;

import java.util.List;

public interface ContractInfoService {
    List<ContractInfoDto> getAll();
    ContractInfoDto getById(Long id);
    ContractInfoDto create(ContractInfoDto dto);
    ContractInfoDto update(Long id, ContractInfoDto dto);
    void delete(Long id);

    ContractInfoDto getByEmployeeId(Long employeeId);

    ContractInfoDto updateByEmployeeId(Long employeeId, ContractInfoDto dto);
}
