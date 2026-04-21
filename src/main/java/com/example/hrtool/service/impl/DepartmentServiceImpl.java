package com.example.hrtool.service.impl;

import com.example.hrtool.dto.*;
import com.example.hrtool.model.*;
import com.example.hrtool.repository.*;
import com.example.hrtool.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository repository;

    public List<NamedEntityDto> getAll() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }
    public NamedEntityDto create(NamedEntityDto dto) {
        return toDto(repository.save(toEntity(dto)));
    }
    public NamedEntityDto update(Long id, NamedEntityDto dto) {
        Department d = toEntity(dto);
        d.setId(id);
        return toDto(repository.save(d));
    }
    public void delete(Long id) {
        repository.deleteById(id);
    }
    private NamedEntityDto toDto(Department d) {
        NamedEntityDto dto = new NamedEntityDto();
        dto.setId(d.getId());
        dto.setName(d.getName());
        return dto;
    }
    private Department toEntity(NamedEntityDto dto) {
        Department d = new Department();
        d.setName(dto.getName());
        return d;
    }
}
