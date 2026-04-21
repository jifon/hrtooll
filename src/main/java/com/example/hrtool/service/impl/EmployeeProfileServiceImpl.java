package com.example.hrtool.service.impl;


import com.example.hrtool.dto.EmployeeProfileDto;
import com.example.hrtool.dto.SimpleEmployeeDto;
import com.example.hrtool.helper.EmployeePatchHelper;
import com.example.hrtool.model.*;
import com.example.hrtool.model.enums.ProcessStep;
import com.example.hrtool.repository.*;
import com.example.hrtool.service.EmployeeProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeProfileServiceImpl implements EmployeeProfileService {

    private final EmployeeProfileRepository repository;
    private final EmployeePatchHelper employeePatchHelper;
    private final SystemUserRepository userRepo;
    private final UserProcessedEmployeeRepository userProcessedEmployeeRepo;



    @Override
    public Long createWithNameAndSurname(String firstName, String lastName) {
        EmployeeProfile profile = new EmployeeProfile();
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        profile.setCreatedBy(userRepo.findById(getCurrentUserId())
                .orElseThrow(() -> new RuntimeException("Creator not found")));

        profile.setActiveProcessSteps(Set.of(ProcessStep.RECRUITING));

        profile.setRecruitingCompleted(false);
        profile.setBereichsleiterCompleted(false);
        profile.setHrCompleted(false);
        profile.setKlCompleted(false);
        profile.setItCompleted(false);
        return repository.save(profile).getId();
    }



    @Override
    public void startProcessing(Long employeeId) {

        Long userId = getCurrentUserId();

        EmployeeProfile employee = repository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        SystemUser user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<UserProcessedEmployee> existing =
                userProcessedEmployeeRepo.findByUserIdAndEmployeeId(userId, employeeId);

        if (existing.isPresent()) {
            // time update for existing relation
            UserProcessedEmployee relation = existing.get();
            relation.setProcessedAt(LocalDateTime.now());
            userProcessedEmployeeRepo.save(relation);
        } else {
            // create new relation
            UserProcessedEmployee relation = new UserProcessedEmployee();
            relation.setUser(user);
            relation.setEmployee(employee);
            relation.setProcessedAt(LocalDateTime.now());

            userProcessedEmployeeRepo.save(relation);
        }
    }
    



    private Long getCurrentUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        SystemUser user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        return user.getId();
    }





    @Override
    public List<SimpleEmployeeDto> getAllAbortedProfiles() {
        return repository.findByProcessingAbortedTrue().stream()
                .map(this::toSimpleDto)
                .collect(Collectors.toList());
    }


    private SimpleEmployeeDto toSimpleDto(EmployeeProfile emp) {
        SimpleEmployeeDto dto = new SimpleEmployeeDto();
        dto.setId(emp.getId());
        dto.setFirstName(emp.getFirstName());
        dto.setLastName(emp.getLastName());
        dto.setEmail(emp.getEmail());
        dto.setDepartmentName(
                emp.getDepartment() != null ? emp.getDepartment().getName() : null
        );
        return dto;
    }

    @Override
    public List<EmployeeProfileDto> getAll() {
        return repository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeProfileDto getById(Long id, String role) {
        return repository.findById(id)
                .map(profile -> {

                    EmployeeProfileDto dto = toDto(profile);

                    boolean roleIsActive = profile.getActiveProcessSteps()
                            .stream()
                            .map(Enum::name)
                            .anyMatch(step ->
                                    step.equals(role)
                                            || (step.equals("KAUFL") && role.equals("KL"))
                            );

                    boolean bereichsleiterException =
                            role.equals("BEREICHSLEITER")
                                    && profile.getActiveProcessSteps()
                                    .stream()
                                    .anyMatch(step -> step.name().equals("RECRUITING"));

                    boolean canEdit = roleIsActive || bereichsleiterException;

                    dto.setCanEdit(canEdit);

                    return dto;
                })
                .orElse(null);
    }




    @Override
    public EmployeeProfileDto create(EmployeeProfileDto dto) {
        EmployeeProfile profile = new EmployeeProfile();
        employeePatchHelper.patchEntity(profile, dto);

        profile.setActiveProcessSteps(Set.of(ProcessStep.RECRUITING));

        profile.setRecruitingCompleted(false);
        profile.setBereichsleiterCompleted(false);
        profile.setHrCompleted(false);
        profile.setKlCompleted(false);
        profile.setItCompleted(false);

        return toDto(repository.save(profile));
    }


    @Override
    public EmployeeProfileDto update(Long id, EmployeeProfileDto dto) {
        EmployeeProfile existing = repository.findById(id).orElseThrow();
        employeePatchHelper.patchEntity(existing, dto);

        return toDto(repository.save(existing));
    }


    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<EmployeeProfileDto> filter(String status, String role, String name) {
        return repository.filter(status, role, name).stream().map(this::toDto).collect(Collectors.toList());
    }



    private EmployeeProfileDto toDto(EmployeeProfile entity) {
        EmployeeProfileDto dto = new EmployeeProfileDto();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());

        dto.setGenderId(entity.getGender() != null ? entity.getGender().getId() : null);
        dto.setBirthDate(entity.getBirthDate() != null ? entity.getBirthDate().toString() : null);
        dto.setLinkedinUrl(entity.getLinkedinUrl());
        dto.setEmail(entity.getEmail());
        dto.setResidence(entity.getResidence());
        dto.setPostalCode(entity.getPostalCode());
        dto.setStreet(entity.getStreet());
        dto.setHouseNumber(entity.getHouseNumber());
        dto.setTitleIfTeamLeader(entity.getTitleIfTeamLead());
        dto.setStartDate(entity.getStartDate());
        dto.setPhonePrivate(entity.getPhonePrivate());

        dto.setDegreeId(entity.getDegree() != null ? entity.getDegree().getId() : null);
        dto.setDepartmentId(entity.getDepartment() != null ? entity.getDepartment().getId() : null);
        dto.setLocationId(entity.getLocation() != null ? entity.getLocation().getId() : null);
        dto.setPositionId(entity.getPosition() != null ? entity.getPosition().getId() : null);
        dto.setCreatedById(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dto.setStatusId(entity.getStatus() != null ? entity.getStatus().getId() : null);
        dto.setSubStatusId(entity.getSubStatus() != null ? entity.getSubStatus().getId() : null);

        dto.setCertificationIds(entity.getCertifications() != null
                ? entity.getCertifications().stream().map(Certification::getId).collect(Collectors.toList())
                : null);

        return dto;
    }

}
