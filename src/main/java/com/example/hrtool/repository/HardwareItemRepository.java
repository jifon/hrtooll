package com.example.hrtool.repository;

import com.example.hrtool.model.HardwareItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HardwareItemRepository  extends JpaRepository<HardwareItem, Long> {}

