package com.example.hrtool.repository;

import com.example.hrtool.model.options.StatusOption;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StatusOptionRepository extends JpaRepository<StatusOption, Long> {
    List<StatusOption> findAllByIsActiveTrue();
}
