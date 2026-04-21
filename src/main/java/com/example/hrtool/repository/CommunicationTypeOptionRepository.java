package com.example.hrtool.repository;

import com.example.hrtool.model.options.CommunicationTypeOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunicationTypeOptionRepository extends JpaRepository<CommunicationTypeOption, Long> {
    List<CommunicationTypeOption> findAllByIsActiveTrue();
}
