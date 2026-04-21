package com.example.hrtool.dto;


import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class EmployeeProfileDto {

    // Persönliche Daten
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String nameAbbreviation;
//    private String gender;
    private Long genderId;
    private String birthDate;
    private String residence;
    private String postalCode;
    private String street;
    private String houseNumber;
    private String titleIfTeamLeader;
    private LocalDate startDate;
    private String phonePrivate;
    private Long degreeId;
    private String linkedinUrl;


    // Betriebliche Daten
    private Long statusId;
    private Long subStatusId;
    private Long departmentId;
    private Long locationId;
    private Long createdById;

    // Qualifizierende Daten
    private Long positionId;
    private List<Long> certificationIds;

    // Kaufmännische Daten
    private Long contractTypeId;
    private Long paymentTypeId;
    private Long probationId;
    private String salaryMatrix; // JSON как строка
    private Integer workingHoursPerWeek;
    private Long workingDaysPerWeekId;

    // Inventardaten
    private String communication;
    private String hardware; // JSON как строка
    private String software; // JSON как строка

    // Zugangsdaten
    //private String nameAbbreviation;
    private String personnelNumber;
    private String sapNumber;
    //private String email;
    private String businessPhone;

    private boolean canEdit;



}

