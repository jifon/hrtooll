package com.example.hrtool.service;


import com.example.hrtool.dto.AccessDataDto;
import com.example.hrtool.model.AccessData;

import java.util.List;

public interface AccessDataService {
    List<AccessDataDto> getAll();
    AccessDataDto create(AccessDataDto dto);
    AccessDataDto update(Long id, AccessDataDto dto);
    void delete(Long id);
    AccessDataDto findByEmployeeId(Long id);
}
