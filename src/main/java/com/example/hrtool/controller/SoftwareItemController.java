package com.example.hrtool.controller;

import com.example.hrtool.dto.*;
import com.example.hrtool.payload.BaseResponse;
import com.example.hrtool.service.SoftwareItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/software")
@RequiredArgsConstructor
public class SoftwareItemController {
    private final SoftwareItemService service;

    @GetMapping
    public BaseResponse<List<SoftwareItemDto>> getAll() {
        return new BaseResponse<>(true, "OK", service.getAll());
    }
    @PostMapping
    public BaseResponse<SoftwareItemDto> create(@RequestBody SoftwareItemDto dto) {
        return new BaseResponse<>(true, "Created", service.create(dto));
    }
    @PutMapping("/{id}")
    public BaseResponse<SoftwareItemDto> update(@PathVariable Long id, @RequestBody SoftwareItemDto dto) {
        return new BaseResponse<>(true, "Updated", service.update(id, dto));
    }
    @DeleteMapping("/{id}")
    public BaseResponse<String> delete(@PathVariable Long id) {
        service.delete(id);
        return new BaseResponse<>(true, "Deleted", null);
    }
}
