package com.example.hrtool.repository;

import com.example.hrtool.model.options.StatusOption;
import com.example.hrtool.model.options.VertragsartOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VertragsartOptionRepository extends JpaRepository<VertragsartOption, Long> {
    List<VertragsartOption> findAllByIsActiveTrue();

}
