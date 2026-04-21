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
public class LocationServiceImpl implements LocationService {
    private final LocationRepository repository;

    public List<NamedEntityDto> getAll() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }
    public NamedEntityDto create(NamedEntityDto dto) {
        return toDto(repository.save(toEntity(dto)));
    }
    public NamedEntityDto update(Long id, NamedEntityDto dto) {
        Location l = toEntity(dto);
        l.setId(id);
        return toDto(repository.save(l));
    }
    public void delete(Long id) {
        repository.deleteById(id);
    }
    private NamedEntityDto toDto(Location l) {
        NamedEntityDto dto = new NamedEntityDto();
        dto.setId(l.getId());
        dto.setName(l.getName());
        return dto;
    }
    private Location toEntity(NamedEntityDto dto) {
        Location l = new Location();
        l.setName(dto.getName());
        return l;
    }
}
