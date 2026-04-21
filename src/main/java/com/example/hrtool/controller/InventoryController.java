package com.example.hrtool.controller;


import com.example.hrtool.dto.*;
import com.example.hrtool.payload.BaseResponse;
import com.example.hrtool.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService service;

    @GetMapping
    public BaseResponse<List<InventoryDto>> getAll() {
        return new BaseResponse<>(true, "OK", service.getAll());
    }
    @GetMapping("/{id}")
    public BaseResponse<InventoryDto> get(@PathVariable Long id) {
        return new BaseResponse<>(true, "OK", service.getById(id));
    }
    @PostMapping
    public BaseResponse<InventoryDto> create(@RequestBody InventoryDto dto) {
        return new BaseResponse<>(true, "Created", service.create(dto));
    }
    @PutMapping("/{id}")
    public BaseResponse<InventoryDto> update(@PathVariable Long id, @RequestBody InventoryDto dto) {
        return new BaseResponse<>(true, "Updated", service.update(id, dto));
    }

    @PutMapping("/by-employee/{employeeId}")
    public BaseResponse<InventoryDto> updateByEmployee(@PathVariable Long employeeId, @RequestBody InventoryDto dto) {
        return new BaseResponse<>(true, "Updated", service.updateByEmployeeId(employeeId, dto));
    }

    @GetMapping("/by-employee/{employeeId}")
    public BaseResponse<InventoryDto> getByEmployee(@PathVariable Long employeeId) {
        return new BaseResponse<>(true, "OK", service.getByEmployeeId(employeeId));
    }

    @DeleteMapping("/{id}")
    public BaseResponse<String> delete(@PathVariable Long id) {
        service.delete(id);
        return new BaseResponse<>(true, "Deleted", null);
    }
}
