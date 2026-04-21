package com.example.hrtool.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "system_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name="systemUserGenerator", initialValue=1, allocationSize=1)
public class SystemUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "systemUserGenerator")
    @Column(nullable = false, updatable = false)
    private Long id;

    private String email;
    private String fullName;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String password;

    private boolean active;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserProcessedEmployee> processedEmployees = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RefreshToken> refreshTokenList;

    public enum Role {
        OTHERS, HR, RECRUITING, BEREICHSLEITER, IT, KL, MARKETING, ADMIN
    }
}
