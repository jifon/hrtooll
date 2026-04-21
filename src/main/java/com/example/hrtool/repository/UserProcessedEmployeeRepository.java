package com.example.hrtool.repository;

import com.example.hrtool.model.UserProcessedEmployee;
import com.example.hrtool.model.enums.ProcessStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface UserProcessedEmployeeRepository extends JpaRepository<UserProcessedEmployee, Long> {

    Optional<UserProcessedEmployee> findByUserIdAndEmployeeId(Long userId, Long employeeId);

    @Query("""
    SELECT upe 
    FROM UserProcessedEmployee upe
    JOIN upe.employee e
    WHERE upe.user.id = :userId
    AND :role MEMBER OF e.activeProcessSteps
    ORDER BY upe.processedAt DESC
""")
    List<UserProcessedEmployee> findTopProcessedByUserAndRole(
            @Param("userId") Long userId,
            @Param("role") ProcessStep role,
            Pageable pageable
    );
}
