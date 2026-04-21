package com.example.hrtool.service;

import com.example.hrtool.dto.SoftwareItemDto;

import java.util.List;

public interface SoftwareItemService {
    List<SoftwareItemDto> getAll();
    SoftwareItemDto create(SoftwareItemDto dto);
    SoftwareItemDto update(Long id, SoftwareItemDto dto);
    void delete(Long id);
}
