package com.example.hrtool.repository;

import com.example.hrtool.model.EmployeeProfile;
import com.example.hrtool.model.ProfileViewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.List;

public interface ProfileViewStatusRepository
        extends JpaRepository<ProfileViewStatus, Long> {

    Optional<ProfileViewStatus> findByEmployeeIdAndUserId(Long employeeId, Long userId);

    List<ProfileViewStatus> findAllByUserIdAndViewedFalse(Long userId);

    @Query("""
    SELECT ep FROM EmployeeProfile ep
    LEFT JOIN ProfileViewStatus pvs
      ON pvs.employee.id = ep.id AND pvs.user.id = :userId
    WHERE pvs IS NULL OR pvs.viewed = false
    """)
    List<EmployeeProfile> findUnreadProfiles(Long userId);

}
