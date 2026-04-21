package com.example.hrtool.repository;

import com.example.hrtool.model.ContractInfo;
import com.example.hrtool.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContractInfoRepository extends JpaRepository<ContractInfo, Long> {
    Optional<ContractInfo> findByEmployeeId(Long employeeId);
}
