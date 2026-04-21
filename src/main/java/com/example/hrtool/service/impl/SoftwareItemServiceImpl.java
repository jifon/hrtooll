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
public class SoftwareItemServiceImpl implements SoftwareItemService {
    private final SoftwareItemRepository repository;
    private final InventoryRepository inventoryRepository;

    public List<SoftwareItemDto> getAll() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }
    public SoftwareItemDto create(SoftwareItemDto dto) {
        return toDto(repository.save(toEntity(dto)));
    }
    public SoftwareItemDto update(Long id, SoftwareItemDto dto) {
        SoftwareItem s = toEntity(dto);
        s.setId(id);
        return toDto(repository.save(s));
    }
    public void delete(Long id) {
        repository.deleteById(id);
    }
    private SoftwareItemDto toDto(SoftwareItem s) {
        SoftwareItemDto dto = new SoftwareItemDto();
        dto.setId(s.getId());
        dto.setName(s.getName());
        dto.setCategory(s.getCategory());
        dto.setLicense(s.getLicense());
        dto.setInventoryId(s.getInventory().getId());
        dto.setVersion(s.getVersion());
        return dto;
    }
    private SoftwareItem toEntity(SoftwareItemDto dto) {
        SoftwareItem s = new SoftwareItem();
        s.setName(dto.getName());
        s.setCategory(dto.getCategory());
        s.setLicense(dto.getLicense());
        s.setInventory(inventoryRepository.findById(dto.getInventoryId()).orElse(null));
        s.setVersion(dto.getVersion());
        return s;
    }
}
