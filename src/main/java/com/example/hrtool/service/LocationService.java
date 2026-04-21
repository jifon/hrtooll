package com.example.hrtool.service;

import com.example.hrtool.dto.NamedEntityDto;

import java.util.List;

public interface LocationService {
    List<NamedEntityDto> getAll();
    NamedEntityDto create(NamedEntityDto dto);
    NamedEntityDto update(Long id, NamedEntityDto dto);
    void delete(Long id);
}
