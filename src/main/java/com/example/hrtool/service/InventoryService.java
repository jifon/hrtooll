package com.example.hrtool.service;

import com.example.hrtool.dto.InventoryDto;

import java.util.List;

public interface InventoryService {
    List<InventoryDto> getAll();
    InventoryDto getById(Long id);
    InventoryDto create(InventoryDto dto);
    InventoryDto update(Long id, InventoryDto dto);
    void delete(Long id);

    InventoryDto getByEmployeeId(Long employeeId);
    // метод обновления по employeeId (или создание, если записи нет)
    InventoryDto updateByEmployeeId(Long employeeId, InventoryDto dto);

}
