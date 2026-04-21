package com.example.hrtool.repository;

import com.example.hrtool.model.options.StatusOption;
import com.example.hrtool.model.options.SubStatusOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface SubStatusOptionRepository extends JpaRepository<SubStatusOption, Long> {
    List<SubStatusOption> findAllByIsActiveTrue();

    Collection<SubStatusOption> findAllByParentStatusAndIsActiveTrue(StatusOption status);
}