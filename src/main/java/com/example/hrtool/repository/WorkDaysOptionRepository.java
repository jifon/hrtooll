package com.example.hrtool.repository;

import com.example.hrtool.model.options.StatusOption;
import com.example.hrtool.model.options.WorkDaysOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkDaysOptionRepository extends JpaRepository<WorkDaysOption, Long> {
    List<WorkDaysOption> findAllByIsActiveTrue();

}
