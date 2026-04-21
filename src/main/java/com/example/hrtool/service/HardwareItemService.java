package com.example.hrtool.service;

import com.example.hrtool.dto.HardwareItemDto;
import com.example.hrtool.model.HardwareItem;

import java.util.List;

public interface HardwareItemService {
    List<HardwareItemDto> getAll();
    HardwareItemDto create(HardwareItemDto dto);
    HardwareItemDto update(Long id, HardwareItemDto dto);
    void delete(Long id);
}
