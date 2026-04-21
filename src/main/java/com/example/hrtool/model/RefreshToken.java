package com.example.hrtool.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;


@Entity
@Data
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String refreshToken;

    private Date expiresAt;

    @ManyToOne
    private SystemUser user;
}
