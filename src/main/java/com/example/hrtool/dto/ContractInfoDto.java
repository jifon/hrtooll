package com.example.hrtool.dto;


import com.example.hrtool.model.EmployeeProfile;
import com.example.hrtool.model.enums.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
public class ContractInfoDto {
    private Long id;
    private Long employeeId;
    private Long contractTypeId;
    private Long paymentTypeId;
    private Long probationId;
    private Integer workingHoursPerWeek;
    private Long workingDaysPerWeekId;

    private Boolean companyCar;

    private BigDecimal fixedSalaryDuringProbation;
    private BigDecimal fixedSalaryAfterProbation;

    private BigDecimal variableSalaryDuringProbation;
    private BigDecimal variableSalaryAfterProbation;

    private BigDecimal bonusDuringProbation;
    private BigDecimal bonusAfterProbation;
}
