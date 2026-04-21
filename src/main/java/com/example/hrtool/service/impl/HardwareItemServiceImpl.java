package com.example.hrtool.service.impl;

import com.example.hrtool.dto.HardwareItemDto;
import com.example.hrtool.dto.SoftwareItemDto;
import com.example.hrtool.model.HardwareItem;
import com.example.hrtool.model.SoftwareItem;
import com.example.hrtool.repository.HardwareItemRepository;
import com.example.hrtool.repository.InventoryRepository;
import com.example.hrtool.repository.SoftwareItemRepository;
import com.example.hrtool.service.HardwareItemService;
import com.example.hrtool.service.SoftwareItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class HardwareItemServiceImpl implements HardwareItemService {
    private final HardwareItemRepository repository;
    private final InventoryRepository inventoryRepository;

    public List<HardwareItemDto> getAll() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public HardwareItemDto create(HardwareItemDto dto) {
        return toDto(repository.save(toEntity(dto)));
    }
    public HardwareItemDto update(Long id, HardwareItemDto dto) {
        HardwareItem s = toEntity(dto);
        s.setId(id);
        return toDto(repository.save(s));
    }
    public void delete(Long id) {
        repository.deleteById(id);
    }
    private HardwareItemDto toDto(HardwareItem s) {
        HardwareItemDto dto = new HardwareItemDto();
        dto.setId(s.getId());
        dto.setName(s.getName());
        dto.setInventoryNr(s.getInventoryNr());
        dto.setInventoryId(s.getInventory().getId());
        return dto;
    }
    private HardwareItem toEntity(HardwareItemDto dto) {
        HardwareItem s = new HardwareItem();
        s.setName(dto.getName());
        s.setInventoryNr(dto.getInventoryNr());
        s.setInventory(inventoryRepository.findById(dto.getInventoryId()).orElse(null));
        return s;
    }
}
