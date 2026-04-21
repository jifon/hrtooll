package com.example.hrtool.service.impl;

import com.example.hrtool.dto.SidebarSimpleEmployeeDto;
import com.example.hrtool.model.*;
import com.example.hrtool.model.enums.ProcessStep;
import com.example.hrtool.repository.*;
import com.example.hrtool.service.EmployeeStatusService;

import com.example.hrtool.model.enums.AbortState;
import com.example.hrtool.dto.EmployeeBlockStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EmployeeStatusServiceImpl implements EmployeeStatusService {

    private final EmployeeProfileRepository profileRepository;
    private final ContractInfoRepository contractInfoRepository;
    private final InventoryRepository inventoryRepository;
    private final AccessDataRepository accessDataRepository;
    private final UserProcessedEmployeeRepository userProcessedEmployeeRepo;
    private final SystemUserRepository userRepo;

    @Override
    public List<EmployeeBlockStatusDto> getAllStatus() {
        return profileRepository.findAllByProcessingAbortedFalse().stream()
                .map(this::buildStatusDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeBlockStatusDto> getAdvancedFilteredStatus(String role, String name,
                                                                  Boolean personalData, Boolean companyData,
                                                                  Boolean qualificationData, Boolean financeData,
                                                                  Boolean inventoryData, Boolean accessData, Boolean sapNumber) {
        return getAllStatus().stream()
                .filter(dto -> role == null || dto.getCurrentStatuses().stream()
                .anyMatch(r -> r.equalsIgnoreCase(role)))
                .filter(dto -> name == null || dto.getFullName().toLowerCase().contains(name.toLowerCase()))
                .filter(dto -> personalData == null || dto.isPersonalDataFilled() == personalData)
                .filter(dto -> companyData == null || dto.isCompanyDataFilled() == companyData)
                .filter(dto -> qualificationData == null || dto.isQualificationDataFilled() == qualificationData)
                .filter(dto -> financeData == null || dto.isFinanceDataFilled() == financeData)
                .filter(dto -> inventoryData == null || dto.isInventoryDataFilled() == inventoryData)
                .filter(dto -> accessData == null || dto.isAccessDataFilled() == accessData)
                .filter(dto -> sapNumber == null || dto.isSapNumberFilled() == sapNumber)
                .toList();
    }

    @Override
    public List<SidebarSimpleEmployeeDto> getLastProcessedForMyRole() {

        Long userId = getCurrentUserId();

        SystemUser user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProcessStep role = mapRoleToProcessStep(user.getRole());

        // last 10
        List<UserProcessedEmployee> relations =
                userProcessedEmployeeRepo.findTopProcessedByUserAndRole(
                        userId,
                        role,
                        PageRequest.of(0, 10)
                );

        List<EmployeeProfile> lastProcessed = relations.stream()
                .map(UserProcessedEmployee::getEmployee)
                .filter(emp -> emp.getActiveProcessSteps().contains(role))
                .filter(emp -> !emp.isProcessingAborted())
                .toList();

        Set<Long> usedIds = lastProcessed.stream()
                .map(EmployeeProfile::getId)
                .collect(Collectors.toSet());

        // if 10 last processed exist - return them, если 10 последних достаточно - возвращаем их
        if (lastProcessed.size() >= 10) {
            return lastProcessed.stream()
                    .map(this::toSimpleDtoWithStatus)
                    .toList();
        }

        // additionally, get more employees for this role, excluding already used ones, if last processed less than 10
        int remaining = 10 - lastProcessed.size();

        List<EmployeeProfile> additional = profileRepository.findAllByProcessingAbortedFalse().stream()
                .filter(emp -> emp.getActiveProcessSteps().contains(role))
                .filter(emp -> !usedIds.contains(emp.getId()))
                .limit(remaining)
                .toList();

        // than combine last processed and additionally fetched, to have up to 10 employees for this role
        List<EmployeeProfile> result = new ArrayList<>();
        result.addAll(lastProcessed);
        result.addAll(additional);

        // map to dto and return
        return result.stream()
                .map(this::toSimpleDtoWithStatus)
                .toList();
    }

    private ProcessStep mapRoleToProcessStep(SystemUser.Role role) {
        return switch (role) {
            case HR -> ProcessStep.HR;
            case RECRUITING -> ProcessStep.RECRUITING;
            case BEREICHSLEITER -> ProcessStep.BEREICHSLEITER;
            case IT -> ProcessStep.IT;
            case KL -> ProcessStep.KAUFL;
//            case MARKETING -> ProcessStep.;
            default -> throw new RuntimeException("Unsupported role");
        };
    }

    private SidebarSimpleEmployeeDto toSimpleDtoWithStatus(EmployeeProfile emp) {
        SidebarSimpleEmployeeDto dto = new SidebarSimpleEmployeeDto();
        dto.setId(emp.getId());
        dto.setFullName(emp.getFirstName() + " " + emp.getLastName());
        if (emp.getAbortState() == AbortState.IN_PROGRESS) {
            dto.setAborted("A");
        }

        return dto;
    }



    @Override
    public List<EmployeeBlockStatusDto> getStatusByRole(String role) {
        return profileRepository.findAllByProcessingAbortedFalse().stream()
                .map(this::buildStatusDto)
                .filter(dto -> dto.getCurrentStatuses().stream().anyMatch(r -> r.equalsIgnoreCase(role))
                )
                .collect(Collectors.toList());
    }





    @Override
    public List<EmployeeBlockStatusDto> getFilteredStatus(Boolean personalData, Boolean companyData, Boolean qualificationData,
                                                          Boolean financeData, Boolean inventoryData, Boolean accessData, Boolean sapNumber) {
        return getAllStatus().stream()
                .filter(dto -> personalData == null || dto.isPersonalDataFilled() == personalData)
                .filter(dto -> companyData == null || dto.isCompanyDataFilled() == companyData)
                .filter(dto -> qualificationData == null || dto.isQualificationDataFilled() == qualificationData)
                .filter(dto -> financeData == null || dto.isFinanceDataFilled() == financeData)
                .filter(dto -> inventoryData == null || dto.isInventoryDataFilled() == inventoryData)
                .filter(dto -> accessData == null || dto.isAccessDataFilled() == accessData)
                .filter(dto -> sapNumber == null || dto.isSapNumberFilled() == sapNumber)
                .toList();
    }




    private EmployeeBlockStatusDto buildStatusDto(EmployeeProfile profile) {

        ContractInfo contract = contractInfoRepository.findByEmployeeId(profile.getId()).orElse(null);
        Inventory inventory = inventoryRepository.findByEmployee_Id(profile.getId()).orElse(null);
        AccessData access = accessDataRepository.findAccessDataByEmployeeId(profile.getId()).orElse(null);

        EmployeeBlockStatusDto dto = new EmployeeBlockStatusDto();
        dto.setId(profile.getId());
        dto.setFullName(profile.getFirstName() + " " + profile.getLastName());

        dto.setPersonalDataFilled(isPersonalDataFilled(profile));
        dto.setCompanyDataFilled(isCompanyDataFilled(profile));
        dto.setQualificationDataFilled(isQualificationDataFilled(profile));
        dto.setFinanceDataFilled(contract != null && isFinanceDataFilled(contract));

        dto.setInventoryDataFilled(isInventorySelected(inventory));
        dto.setInventoryNrFilled(isInventoryNrFilled(inventory));

        dto.setHrCompleted(profile.isHrCompleted());
        dto.setKlCompleted(profile.isKlCompleted());
        dto.setItCompleted(profile.isItCompleted());

        dto.setHrAccessFilled(access != null && isAccessPartFilledByHr(access));
        dto.setKlAccessFilled(access != null && isAccessPartFilledByKaufl(access));
        dto.setItAccessFilled(access != null && isAccessPartFilledByIt(access));

        // sapNumberFilled = часть KL
        dto.setSapNumberFilled(access != null && isAccessPartFilledByKaufl(access));

        // accessDataFilled = ВСЕ части (HR+KL+IT)
        dto.setAccessDataFilled(access != null && isAccessDataFilled(access));



        Set<String> statuses = profile.getActiveProcessSteps()
                .stream()
                .map(Enum::name)
                .collect(Collectors.toSet());

        if (profile.getAbortState() == AbortState.IN_PROGRESS) {
            statuses.add("A");
        }

        dto.setCurrentStatuses(statuses);


        // ---- Abort данные для UI ----
        dto.setAbortState(profile.getAbortState() == null ? AbortState.NONE : profile.getAbortState());

        dto.setAbortRequiredRoles(
                profile.getAbortRequiredRoles() == null ? null :
                        profile.getAbortRequiredRoles().stream().map(Enum::name).collect(Collectors.toSet())
        );

        dto.setAbortConfirmedRoles(
                profile.getAbortConfirmedRoles() == null ? null :
                        profile.getAbortConfirmedRoles().stream().map(Enum::name).collect(Collectors.toSet())
        );


        return dto;
    }




    private boolean isPersonalDataFilled(EmployeeProfile profile) {
        return notBlank(profile.getFirstName()) && notBlank(profile.getLastName()) && profile.getGender() != null &&
                profile.getBirthDate() != null && notBlank(profile.getResidence()) && notBlank(profile.getStreet()) &&
                notBlank(profile.getHouseNumber()) && notBlank(profile.getPostalCode()) && notBlank(profile.getPhonePrivate());
    }

    private boolean isCompanyDataFilled(EmployeeProfile profile) {
        return profile.getStatus() != null && profile.getDepartment() != null && profile.getStartDate() != null && profile.getLocation() != null;
    }

    private boolean isQualificationDataFilled(EmployeeProfile profile) {
        return profile.getPosition() != null && profile.getCertifications() != null && !profile.getCertifications().isEmpty();
    }

    private boolean isFinanceDataFilled(ContractInfo contract) {

        if (contract == null) return false;

        if (contract.getContractType() == null ||
                contract.getPaymentType() == null ||
                contract.getWorkingDaysPerWeek() == null ||
                contract.getWorkingHoursPerWeek() == null) {
            return false;
        }


        String paymentType = contract.getPaymentType().getValue().toLowerCase();
        String probation = contract.getProbation() != null
                ? contract.getProbation().getValue().toLowerCase()
                : "";


        boolean hasVariable = paymentType.contains("variable");
        boolean hasBonus = paymentType.contains("prämie") || paymentType.contains("praemie");
        boolean hasProbation = probation.equals("ja");

        // --- DURING ---
        if (contract.getFixedSalaryDuringProbation() == null) return false;

        if (hasVariable && contract.getVariableSalaryDuringProbation() == null) return false;
        if (hasBonus && contract.getBonusDuringProbation() == null) return false;

        // --- AFTER (if Probezeit) ---
        if (hasProbation) {
            if (contract.getFixedSalaryAfterProbation() == null) return false;

            if (hasVariable && contract.getVariableSalaryAfterProbation() == null) return false;
            if (hasBonus && contract.getBonusAfterProbation() == null) return false;
        }

        return true;

//        boolean filledAfter = contract.getFixedSalaryAfterProbation() != null &&
//                contract.getVariableSalaryAfterProbation() != null &&
//                contract.getBonusAfterProbation() != null;
//
//        boolean filledDuring = contract.getFixedSalaryDuringProbation() != null &&
//                contract.getVariableSalaryDuringProbation() != null &&
//                contract.getBonusDuringProbation() != null;
//
//        return contract.getContractType() != null &&
//                contract.getPaymentType() != null &&
//                contract.getWorkingDaysPerWeek() != null &&
//                contract.getWorkingHoursPerWeek() != null &&
//                (filledAfter || filledDuring);

    }

    private boolean isInventorySelected(Inventory inventory) {

        return inventory != null &&
                inventory.getHardwareItems() != null &&
                inventory.getHardwareItems().stream()
                        .anyMatch(item -> notBlank(item.getName()));
    }

    private boolean isInventoryNrFilled(Inventory inventory) {
        if (inventory == null || inventory.getHardwareItems() == null) return false; // ничего не проверяем

        // Названия оборудования, для которых нужно проверять inventoryNr
        Set<String> requiredTypes = Set.of("Laptop", "Dockingstation", "Monitor");

        return inventory.getHardwareItems().stream()
                .filter(item -> item.getName() != null && requiredTypes.contains(item.getName()))
                .allMatch(item -> notBlank(item.getInventoryNr()));

    }


    private boolean notBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private boolean isAccessPartFilledByHr(AccessData access) {
        return notBlank(access.getNameAbbreviation()) && notBlank(access.getPersonnelNumber());
    }

    private boolean isAccessPartFilledByKaufl(AccessData access) {
        return notBlank(access.getSapNumber());
    }

    private boolean isAccessPartFilledByIt(AccessData access) {
        return notBlank(access.getEmail()) && notBlank(access.getBusinessPhone());
    }

    private boolean isAccessDataFilled(AccessData access) {
        return isAccessPartFilledByHr(access)
                && isAccessPartFilledByKaufl(access)
                && isAccessPartFilledByIt(access);
    }



    @Override
    public List<EmployeeBlockStatusDto> getArchived() {
        return profileRepository.findByProcessingAbortedTrue().stream()
                .map(this::buildStatusDto)
                .toList();
    }

    @Override
    public EmployeeBlockStatusDto buildStatus(EmployeeProfile profile) {
        return buildStatusDto(profile);
    }

    private Long getCurrentUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        SystemUser user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        return user.getId();
    }



}



