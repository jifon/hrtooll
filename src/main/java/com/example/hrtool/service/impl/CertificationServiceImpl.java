package com.example.hrtool.service.impl;

import com.example.hrtool.dto.*;
import com.example.hrtool.model.*;
import com.example.hrtool.repository.*;
import com.example.hrtool.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.hrtool.dto.NamedEntityDto;
import com.example.hrtool.model.Certification;

@Service
@RequiredArgsConstructor
public class CertificationServiceImpl implements CertificationService {
    private final CertificationRepository repository;

    public List<NamedEntityDto> getAll() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }
    public NamedEntityDto create(NamedEntityDto dto) {
        return toDto(repository.save(toEntity(dto)));
    }
    public NamedEntityDto update(Long id, NamedEntityDto dto) {
        Certification c = toEntity(dto);
        c.setId(id);
        return toDto(repository.save(c));
    }
    public void delete(Long id) {
        repository.deleteById(id);
    }
    private NamedEntityDto toDto(Certification c) {
        NamedEntityDto dto = new NamedEntityDto();
        dto.setId(c.getId());
        dto.setName(c.getName());
        return dto;
    }
    private Certification toEntity(NamedEntityDto dto) {
        Certification c = new Certification();
        c.setName(dto.getName());
        return c;
    }
}
