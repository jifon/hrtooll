package com.example.hrtool.service;

import com.example.hrtool.dto.AdvanceResult;
import com.example.hrtool.dto.EmployeeBlockStatusDto;
import com.example.hrtool.model.EmployeeProfile;
import com.example.hrtool.model.SystemUser;
import com.example.hrtool.model.enums.ProcessStep;
import com.example.hrtool.repository.EmployeeProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class NextProcessStepService implements NextProcessStepUseCase {

    private final EmployeeProfileRepository repository;
    private final BlockValidationService validationService;
    private final EmployeeStatusService employeeStatusService;

    public NextProcessStepService(
            EmployeeProfileRepository repository,
            BlockValidationService validationService, EmployeeStatusService employeeStatusService
    ) {
        this.repository = repository;
        this.validationService = validationService;
        this.employeeStatusService = employeeStatusService;
    }

    @Override
    public AdvanceResult proceed(Long employeeId, SystemUser.Role role) {

        EmployeeProfile employee = repository.findById(employeeId)
                .orElseThrow(() -> new IllegalStateException("Employee not found"));

        //Строим актуальный DTO
        EmployeeBlockStatusDto dto =
                employeeStatusService.buildStatus(employee);

        // Проверка допуска роли
        validateRolePermission(employee, role);

        //Бизнес-валидация блоков
        List<String> missing = validateBusinessLogic(role, dto);

        //Если всё ок — фиксируем completion
        if (missing.isEmpty()) {
            markCompleted(employee, role);
            dto = employeeStatusService.buildStatus(employee);
        }

        //Пересчитываем state machine
        Set<ProcessStep> newSteps =
                ProcessStateResolver.resolve(employee, dto);
        // ⚠ ВАЖНО: не заменяем Set, а изменяем существующий
        employee.getActiveProcessSteps().clear();
        employee.getActiveProcessSteps().addAll(newSteps);


        repository.save(employee);

        return new AdvanceResult(missing);
    }

    // =====================================================
    // 🔒 ROLE PERMISSION CHECK
    // =====================================================

    private void validateRolePermission(EmployeeProfile employee,
                                        SystemUser.Role role) {

        Set<ProcessStep> activeSteps = employee.getActiveProcessSteps();

        boolean roleIsActive =
                activeSteps.contains(mapRoleToProcessStep(role));


        // Ausnahme:
        // Bereichsleiter darf übernehmen, wenn Recruiting aktiv ist
        boolean bereichsleiterException =
                role == SystemUser.Role.BEREICHSLEITER
                        && activeSteps.contains(ProcessStep.RECRUITING);

        if (!roleIsActive && !bereichsleiterException) {
            throw new IllegalStateException(
                    "Rolle " + role +
                            " ist aktuell nicht berechtigt, den Prozess weiterzuführen."
            );
        }
    }

    private ProcessStep mapRoleToProcessStep(SystemUser.Role role) {
        return switch (role) {
            case RECRUITING -> ProcessStep.RECRUITING;
            case BEREICHSLEITER -> ProcessStep.BEREICHSLEITER;
            case HR -> ProcessStep.HR;
            case KL -> ProcessStep.KAUFL;
            case IT -> ProcessStep.IT;
            default -> throw new IllegalStateException("Unknown role: " + role);
        };
    }


    // =====================================================
    // 📝 BUSINESS VALIDATION
    // =====================================================

    private List<String> validateBusinessLogic(SystemUser.Role role,
                                               EmployeeBlockStatusDto dto) {

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
                if (!dto.isInventoryDataFilled()) {
                    missing.add("Inventarauswahl");
                }
                // Finance darf schon KL aktivieren
                if (!dto.isFinanceDataFilled()) {
                    missing.add("Kommerzielle Daten");
                }
            }

            case KL -> {
                if (!dto.isSapNumberFilled()) {
                    missing.add("SAP-Nummer");
                }
            }

            case HR -> {
                if (!dto.isHrAccessFilled()) {
                    missing.add("Namenskürzel / Personalnummer");
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

    // =====================================================
    // 🏁 COMPLETION FLAGS
    // =====================================================

    private void markCompleted(EmployeeProfile e, SystemUser.Role role) {
        switch (role) {
            case RECRUITING -> e.setRecruitingCompleted(true);
            case BEREICHSLEITER -> e.setBereichsleiterCompleted(true);
            case HR -> e.setHrCompleted(true);
            case KL -> e.setKlCompleted(true);
            case IT -> e.setItCompleted(true);
        }
    }
}

