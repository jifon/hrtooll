package com.example.hrtool.model;


import com.example.hrtool.model.enums.AbortState;
import com.example.hrtool.model.enums.ProcessStep;
import com.example.hrtool.model.options.DegreeOption;
import com.example.hrtool.model.options.GenderOption;
import com.example.hrtool.model.options.StatusOption;
import com.example.hrtool.model.options.SubStatusOption;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(name = "current_assigned_role")
    private String currentAssignedRole;

    private String firstName;
    private String lastName;

    private String email;
    private String nameAbbreviation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AbortState abortState = AbortState.NONE;

    @ManyToOne
    @JsonIgnore
    private SystemUser abortInitiatedBy;

    private LocalDateTime abortInitiatedAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name="employee_abort_required_roles",
            joinColumns=@JoinColumn(name="employee_id"))
    @Column(name="role")
    private Set<SystemUser.Role> abortRequiredRoles = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name="employee_abort_confirmed_roles",
            joinColumns=@JoinColumn(name="employee_id"))
    @Column(name="role")
    private Set<SystemUser.Role> abortConfirmedRoles = new HashSet<>();

    @Column(nullable = false)
    private boolean processingAborted = false;

    @ManyToOne
    private GenderOption gender;

    private LocalDate birthDate;
    private String residence;
    private String phonePrivate;
    private String linkedinUrl;

    @ManyToOne
    private DegreeOption degree;

    @ManyToOne
    private StatusOption status;

    @ManyToOne
    private SubStatusOption subStatus;

    @ManyToOne
    private Department department;

    @ManyToOne
    private Location location;

    @ManyToOne
    @JsonIgnore
    private SystemUser createdBy;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;

    private String postalCode;
    private String street;
    private String houseNumber;
    private String titleIfTeamLead;

    private LocalDate startDate;

    @ManyToOne
    @JsonIgnore
    private SystemUser currentlyProcessedBy; // кто сейчас обрабатывает






    @ManyToMany
    @JoinTable(
            name = "employee_certifications",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "certification_id")
    )
    private Set<Certification> certifications;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "employee_active_process_steps",
            joinColumns = @JoinColumn(name = "employee_id")
    )
    @Column(name = "process_step")
    private Set<ProcessStep> activeProcessSteps = new HashSet<>();

    //поля для отслеживания, какие блоки были заполнены и кем
    @Column(nullable = false)
    private boolean recruitingCompleted = false;

    @Column(nullable = false)
    private boolean bereichsleiterCompleted = false;

    @Column(nullable = false)
    private boolean hrCompleted = false;

    @Column(nullable = false)
    private boolean klCompleted = false;

    @Column(nullable = false)
    private boolean itCompleted = false;


}

