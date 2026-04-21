package com.example.hrtool.repository;

import com.example.hrtool.model.AccessData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessDataRepository extends JpaRepository<AccessData, Long> {
    Optional<AccessData> findAccessDataByEmployeeId(Long id);
}

