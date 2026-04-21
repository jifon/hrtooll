package com.example.hrtool.repository;

import com.example.hrtool.model.EmployeeProfile;
import com.example.hrtool.model.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {
    Optional<SystemUser> findByEmail(String email);

    boolean existsBy();
}
