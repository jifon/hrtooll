package com.example.hrtool.model;

import com.example.hrtool.model.enums.*;
import com.example.hrtool.model.options.ProbezeitOption;
import com.example.hrtool.model.options.VertragsartOption;
import com.example.hrtool.model.options.VertragstypOption;
import com.example.hrtool.model.options.WorkDaysOption;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    @OneToOne
    private EmployeeProfile employee;

//    @Enumerated(EnumType.STRING)
//    private Vertragstyp contractType;
    @ManyToOne
    private VertragstypOption contractType;


//    @Enumerated(EnumType.STRING)
//    private Vertragsart paymentType;
    @ManyToOne
    private VertragsartOption paymentType;

//    @Enumerated(EnumType.STRING)
//    private Probezeit probation;
    @ManyToOne
    private ProbezeitOption probation;
    private Integer workingHoursPerWeek;

//    @Enumerated(EnumType.STRING)
//    private WorkDays workingDaysPerWeek;
    @ManyToOne
    private WorkDaysOption workingDaysPerWeek;

    private Boolean companyCar;

    private BigDecimal fixedSalaryDuringProbation;
    private BigDecimal fixedSalaryAfterProbation;

    private BigDecimal variableSalaryDuringProbation;
    private BigDecimal variableSalaryAfterProbation;

    private BigDecimal bonusDuringProbation;
    private BigDecimal bonusAfterProbation;
}
