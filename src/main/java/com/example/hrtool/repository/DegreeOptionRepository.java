package com.example.hrtool.repository;

import com.example.hrtool.model.options.DegreeOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface DegreeOptionRepository extends JpaRepository<DegreeOption, Long> {
    Optional<DegreeOption> findDegreeOptionById(Long id);
    List<DegreeOption> findAllByIsActiveTrue();

}
