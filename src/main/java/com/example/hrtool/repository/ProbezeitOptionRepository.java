package com.example.hrtool.repository;

import com.example.hrtool.model.options.ProbezeitOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProbezeitOptionRepository extends JpaRepository<ProbezeitOption, Long> {
    List<ProbezeitOption> findAllByIsActiveTrue();
}
