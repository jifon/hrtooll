package com.example.hrtool.repository;

import com.example.hrtool.model.EmployeeProfile;
import com.example.hrtool.model.enums.AbortState;
import com.example.hrtool.model.SystemUser;
import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {

    @Query("SELECT e FROM EmployeeProfile e WHERE (:status IS NULL OR e.status = :status) " +
            "AND (:role IS NULL OR e.createdBy.role = :role) " +
            "AND (:name IS NULL OR LOWER(CONCAT(e.firstName, ' ', e.lastName)) LIKE LOWER(CONCAT('%', :name, '%')))")
    List<EmployeeProfile> filter(@Param("status") String status,
                                 @Param("role") String role,
                                 @Param("name") String name);

    Optional<EmployeeProfile> findEmployeeProfileById(Long id);

    // Найти всех, кого в данный момент обрабатывает пользователь
    List<EmployeeProfile> findByCurrentlyProcessedBy(SystemUser user);

//    // Найти всех, кого отменил пользователь
//    List<EmployeeProfile> findByAbortedBy(SystemUser user);

//    // Найти всех, кого пользователь когда-либо обрабатывал (обрабатывал или отменял)
//    @Query("SELECT e FROM EmployeeProfile e WHERE e.currentlyProcessedBy = :user OR e.abortedBy = :user")
//    List<EmployeeProfile> findAllHandledBy(@Param("user") SystemUser user);

    List<EmployeeProfile> findByProcessingAbortedTrue();

    List<EmployeeProfile> findAllByProcessingAbortedFalse();

    List<EmployeeProfile> findByAbortState(AbortState state);

    List<EmployeeProfile> findByAbortStateNot(AbortState state);

    List<EmployeeProfile> findAllByAbortState(AbortState abortState);



}

