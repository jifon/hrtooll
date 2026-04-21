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
public class PositionServiceImpl implements PositionService {
    private final PositionRepository repository;

    public List<NamedEntityDto> getAll() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }
    public NamedEntityDto create(NamedEntityDto dto) {
        return toDto(repository.save(toEntity(dto)));
    }
    public NamedEntityDto update(Long id, NamedEntityDto dto) {
        Position p = toEntity(dto);
        p.setId(id);
        return toDto(repository.save(p));
    }
    public void delete(Long id) {
        repository.deleteById(id);
    }
    private NamedEntityDto toDto(Position p) {
        NamedEntityDto dto = new NamedEntityDto();
        dto.setId(p.getId());
        dto.setName(p.getName());
        return dto;
    }
    private Position toEntity(NamedEntityDto dto) {
        Position p = new Position();
        p.setName(dto.getName());
        return p;
    }
}
