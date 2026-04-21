package com.example.hrtool.service.impl;


import com.example.hrtool.dto.ContractInfoDto;
import com.example.hrtool.model.ContractInfo;
import com.example.hrtool.repository.*;
import com.example.hrtool.service.ContractInfoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContractInfoServiceImpl implements ContractInfoService {
    private final ContractInfoRepository repository;
    private final EmployeeProfileRepository employeeRepository;
    private final VertragstypOptionRepository vertragstypOptionRepository;
    private final VertragsartOptionRepository vertragsartOptionRepository;
    private final ProbezeitOptionRepository probezeitOptionRepository;
    private final WorkDaysOptionRepository workDaysOptionRepository;


    public List<ContractInfoDto> getAll() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }


    public ContractInfoDto getById(Long id) {
        return repository.findById(id).map(this::toDto).orElse(null);
    }


    public ContractInfoDto create(ContractInfoDto dto) {
        return toDto(repository.save(toEntity(dto)));
    }


    public ContractInfoDto update(Long id, ContractInfoDto dto) {
        ContractInfo entity = toEntity(dto);
        entity.setId(id);
        return toDto(repository.save(entity));
    }


    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public ContractInfoDto getByEmployeeId(Long employeeId) {
        return repository.findByEmployeeId(employeeId).map(this::toDto).orElse(null);
    }


    @Override
    public ContractInfoDto updateByEmployeeId(Long employeeId, ContractInfoDto dto) {

        ContractInfo existing = repository.findByEmployeeId(employeeId).orElse(null);

        if (existing == null) {
            ContractInfo newContract = toEntity(dto);
            newContract.setEmployee(employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new EntityNotFoundException("Employee not found")));
            return toDto(repository.save(newContract));
        }

        // Vertragstyp (null = очистить)
        if (dto.getContractTypeId() == null) {
            existing.setContractType(null);
        } else {
            existing.setContractType(
                    vertragstypOptionRepository.findById(dto.getContractTypeId())
                            .orElseThrow(() -> new EntityNotFoundException("Vertragstyp not found"))
            );
        }

        //  Vertragsart (null = очистить)
        if (dto.getPaymentTypeId() == null) {
            existing.setPaymentType(null);
        } else {
            existing.setPaymentType(
                    vertragsartOptionRepository.findById(dto.getPaymentTypeId())
                            .orElseThrow(() -> new EntityNotFoundException("Vertragsart not found"))
            );
        }

        //  Probezeit (null = очистить)
        if (dto.getProbationId() == null) {
            existing.setProbation(null);
        } else {
            existing.setProbation(
                    probezeitOptionRepository.findById(dto.getProbationId())
                            .orElseThrow(() -> new EntityNotFoundException("Probezeit not found"))
            );
        }

        //  WorkDays (null = очистить)
        if (dto.getWorkingDaysPerWeekId() == null) {
            existing.setWorkingDaysPerWeek(null);
        } else {
            existing.setWorkingDaysPerWeek(
                    workDaysOptionRepository.findById(dto.getWorkingDaysPerWeekId())
                            .orElseThrow(() -> new EntityNotFoundException("WorkDays not found"))
            );
        }

        existing.setWorkingHoursPerWeek(dto.getWorkingHoursPerWeek());
        existing.setCompanyCar(dto.getCompanyCar());

        existing.setFixedSalaryDuringProbation(dto.getFixedSalaryDuringProbation());
        existing.setFixedSalaryAfterProbation(dto.getFixedSalaryAfterProbation());

        existing.setVariableSalaryDuringProbation(dto.getVariableSalaryDuringProbation());
        existing.setVariableSalaryAfterProbation(dto.getVariableSalaryAfterProbation());

        existing.setBonusDuringProbation(dto.getBonusDuringProbation());
        existing.setBonusAfterProbation(dto.getBonusAfterProbation());

        return toDto(repository.save(existing));
    }



    private ContractInfoDto toDto(ContractInfo e) {
        ContractInfoDto dto = new ContractInfoDto();
        dto.setId(e.getId());
        dto.setEmployeeId(e.getEmployee().getId());
        dto.setContractTypeId(
                e.getContractType() != null ? e.getContractType().getId() : null
        );
//        dto.setPaymentType(e.getPaymentType());
        dto.setPaymentTypeId(
                e.getPaymentType() != null ? e.getPaymentType().getId() : null
        );
//        dto.setProbation(e.getProbation());
        dto.setProbationId(
                e.getProbation() != null ? e.getProbation().getId() : null
        );

        dto.setWorkingHoursPerWeek(e.getWorkingHoursPerWeek());
//        dto.setWorkingDaysPerWeek(e.getWorkingDaysPerWeek());

        dto.setWorkingDaysPerWeekId(
                e.getWorkingDaysPerWeek() != null ? e.getWorkingDaysPerWeek().getId() : null
        );
        dto.setCompanyCar(e.getCompanyCar());
        dto.setFixedSalaryAfterProbation(e.getFixedSalaryAfterProbation());
        dto.setFixedSalaryDuringProbation(e.getFixedSalaryDuringProbation());
        dto.setVariableSalaryAfterProbation(e.getVariableSalaryAfterProbation());
        dto.setVariableSalaryDuringProbation(e.getVariableSalaryDuringProbation());
        dto.setBonusDuringProbation(e.getBonusDuringProbation());
        dto.setBonusAfterProbation(e.getBonusAfterProbation());

        return dto;
    }


    private ContractInfo toEntity(ContractInfoDto dto) {
        ContractInfo c = new ContractInfo();
        c.setEmployee(employeeRepository.findById(dto.getEmployeeId()).orElse(null));
        if (dto.getContractTypeId() != null) {
            c.setContractType(
                    vertragstypOptionRepository.findById(dto.getContractTypeId())
                            .orElseThrow(() -> new EntityNotFoundException("Vertragstyp not found"))
            );
        }

//        c.setPaymentType(dto.getPaymentType());
        if (dto.getPaymentTypeId() != null) {
            c.setPaymentType(
                    vertragsartOptionRepository.findById(dto.getPaymentTypeId())
                            .orElseThrow(() -> new EntityNotFoundException("Vertragsart not found"))
            );
        }

//        c.setProbation(dto.getProbation());
        if (dto.getProbationId() != null) {
            c.setProbation(
                    probezeitOptionRepository.findById(dto.getProbationId())
                            .orElseThrow(() -> new EntityNotFoundException("Probezeit not found"))
            );
        }
        c.setWorkingHoursPerWeek(dto.getWorkingHoursPerWeek());
//        c.setWorkingDaysPerWeek(dto.getWorkingDaysPerWeek());
        if (dto.getWorkingDaysPerWeekId() != null) {
            c.setWorkingDaysPerWeek(
                    workDaysOptionRepository.findById(dto.getWorkingDaysPerWeekId())
                            .orElseThrow(() -> new EntityNotFoundException("WorkDays not found"))
            );
        }
        c.setCompanyCar(dto.getCompanyCar());
        c.setFixedSalaryAfterProbation(dto.getFixedSalaryAfterProbation());
        c.setFixedSalaryDuringProbation(dto.getFixedSalaryDuringProbation());
        c.setVariableSalaryAfterProbation(dto.getVariableSalaryAfterProbation());
        c.setVariableSalaryDuringProbation(dto.getVariableSalaryDuringProbation());
        c.setBonusDuringProbation(dto.getBonusDuringProbation());
        c.setBonusAfterProbation(dto.getBonusAfterProbation());
        return c;
    }
}
