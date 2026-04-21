package com.example.hrtool.repository;

import com.example.hrtool.model.Department;
import com.example.hrtool.model.options.GenderOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface GenderOptionRepository extends JpaRepository<GenderOption, Long> {
    Optional<GenderOption> findGenderOptionById(Long id);
    List<GenderOption> findAllByIsActiveTrue();
}