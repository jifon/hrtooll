package com.example.hrtool.helper;


import com.example.hrtool.dto.EmployeeProfileDto;
import com.example.hrtool.model.EmployeeProfile;
import com.example.hrtool.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class EmployeePatchHelper {

    private final DepartmentRepository departmentRepository;
    private final GenderOptionRepository genderOptionRepository;
    private final DegreeOptionRepository degreeOptionRepository;
    private final LocationRepository locationRepository;
    private final PositionRepository positionRepository;
    private final CertificationRepository certificationRepository;
    private final StatusOptionRepository statusOptionRepository;
    private final SubStatusOptionRepository subStatusOptionRepository;


    public void patchEntity(EmployeeProfile entity, EmployeeProfileDto dto) {
        if (dto.getFirstName() != null) entity.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) entity.setLastName(dto.getLastName());
        if (dto.getGenderId() != null && dto.getGenderId() > 0)
            entity.setGender(genderOptionRepository.findById(dto.getGenderId()).orElse(null));
        if (dto.getEmail() != null) entity.setEmail(dto.getEmail());
        if (dto.getBirthDate() != null && !dto.getBirthDate().isBlank()) {
            try {
                entity.setBirthDate(LocalDate.parse(dto.getBirthDate()));
            } catch (DateTimeParseException e) {
                throw new RuntimeException("Ungültiges Geburtsdatum: " + dto.getBirthDate());
            }
        }
        if (dto.getResidence() != null) entity.setResidence(dto.getResidence());
        if (dto.getPhonePrivate() != null) entity.setPhonePrivate(dto.getPhonePrivate());
        if (dto.getDegreeId() != null && dto.getDegreeId() > 0)
            entity.setDegree(degreeOptionRepository.findById(dto.getDegreeId()).orElse(null));


        if (dto.getStatusId() != null && dto.getStatusId() > 0)
            entity.setStatus(statusOptionRepository.findById(dto.getStatusId()).orElse(null));

        if (dto.getSubStatusId() != null && dto.getSubStatusId() > 0)
            entity.setSubStatus(subStatusOptionRepository.findById(dto.getSubStatusId()).orElse(null));



        if (dto.getPostalCode() != null) entity.setPostalCode(dto.getPostalCode());
        if (dto.getStreet() != null) entity.setStreet(dto.getStreet());
        if (dto.getHouseNumber() != null) entity.setHouseNumber(dto.getHouseNumber());
        if (dto.getTitleIfTeamLeader() != null) entity.setTitleIfTeamLead(dto.getTitleIfTeamLeader());
        if (dto.getStartDate() != null) entity.setStartDate(dto.getStartDate());

        if (dto.getDepartmentId() != null && dto.getDepartmentId() > 0)
            entity.setDepartment(departmentRepository.findById(dto.getDepartmentId()).orElse(null));

        if (dto.getLocationId() != null && dto.getLocationId() > 0)
            entity.setLocation(locationRepository.findById(dto.getLocationId()).orElse(null));


        if (dto.getPositionId() != null)
            entity.setPosition(positionRepository.findById(dto.getPositionId()).orElse(null));

        if (dto.getCertificationIds() != null)
            entity.setCertifications(new HashSet<>(certificationRepository.findAllById(dto.getCertificationIds())));
    }
}


