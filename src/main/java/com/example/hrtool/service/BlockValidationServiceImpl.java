package com.example.hrtool.service;

import com.example.hrtool.dto.EmployeeBlockStatusDto;
import com.example.hrtool.model.EmployeeProfile;
import com.example.hrtool.model.SystemUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlockValidationServiceImpl implements BlockValidationService {

    private final EmployeeStatusService employeeStatusService;


    @Override
    public void validate(EmployeeProfile employee, SystemUser.Role role) {

        // строим DTO с актуальным состоянием блоков
        EmployeeBlockStatusDto dto = employeeStatusService
                .getAllStatus()
                .stream()
                .filter(d -> d.getId().equals(employee.getId()))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException("Status DTO not found for employee " + employee.getId())
                );

        // проверяем блоки строго по роли
        List<String> missing = getMissingBlocks(role, dto);

        if (!missing.isEmpty()) {
            throw new IllegalStateException(
                    "Fehlende Pflichtfelder für Rolle " + role + ": " + String.join(", ", missing)
            );
        }
    }

    private List<String> getMissingBlocks(SystemUser.Role role, EmployeeBlockStatusDto dto) {

        List<String> missing = new ArrayList<>();

        switch (role) {

            case RECRUITING -> {
                if (!dto.isPersonalDataFilled()) {
                    missing.add("Persönliche Daten");
                }
                if (!dto.isCompanyDataFilled()) {
                    missing.add("Betriebliche Daten");
                }
            }

            case BEREICHSLEITER -> {
                if (!dto.isQualificationDataFilled()) {
                    missing.add("Qualifikation");
                }
                if (!dto.isFinanceDataFilled()) {
                    missing.add("Kommerzielle Daten");
                }
                if (!dto.isInventoryDataFilled()) {
                    missing.add("Inventarauswahl");
                }
            }

            case HR -> {
                if (!dto.isHrAccessFilled()) {
                    missing.add("Namekürzel / Personalnummer");
                }
            }

            case KL -> {
                if (!dto.isSapNumberFilled()) {
                    missing.add("SAP-Nummer");
                }
            }

            case IT -> {
                if (!dto.isInventoryNrFilled()) {
                    missing.add("Inventarnummern");
                }
                if (!dto.isItAccessFilled()) {
                    missing.add("IT-Zugangsdaten");
                }
            }

            default -> throw new IllegalStateException("Unsupported role: " + role);
        }

        return missing;
    }
}

