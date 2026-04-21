package com.example.hrtool.service;

import com.example.hrtool.dto.SimpleNameDto;
import java.util.List;

public interface SimpleEntityService {
    List<SimpleNameDto> getAll();
    SimpleNameDto getById(Long id);
    SimpleNameDto create(SimpleNameDto dto);
    SimpleNameDto update(Long id, SimpleNameDto dto);
    void delete(Long id);
}
