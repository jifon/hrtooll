package com.example.hrtool.controller;


import com.example.hrtool.dto.*;
import com.example.hrtool.payload.BaseResponse;
import com.example.hrtool.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
public class ContractInfoController {
    private final ContractInfoService service;

    @GetMapping
    public BaseResponse<List<ContractInfoDto>> getAll() {
        return new BaseResponse<>(true, "OK", service.getAll());
    }

    @GetMapping("/{id}")
    public BaseResponse<ContractInfoDto> get(@PathVariable Long id) {
        return new BaseResponse<>(true, "OK", service.getById(id));
    }

    @GetMapping("/by-employee/{employeeId}")
    public BaseResponse<ContractInfoDto> getByEmployee(@PathVariable Long employeeId) {
        return new BaseResponse<>(true, "OK", service.getByEmployeeId(employeeId));
    }

    @PostMapping
    public BaseResponse<ContractInfoDto> create(@RequestBody ContractInfoDto dto) {
        return new BaseResponse<>(true, "Created", service.create(dto));
    }

    @PutMapping("/{id}")
    public BaseResponse<ContractInfoDto> update(@PathVariable Long id, @RequestBody ContractInfoDto dto) {
        return new BaseResponse<>(true, "Updated", service.update(id, dto));
    }

    @PutMapping("/by-employee/{employeeId}")
    public BaseResponse<ContractInfoDto> updateByEmployee(@PathVariable Long employeeId, @RequestBody ContractInfoDto dto) {
        return new BaseResponse<>(true, "Updated", service.updateByEmployeeId(employeeId, dto));
    }


    @DeleteMapping("/{id}")
    public BaseResponse<String> delete(@PathVariable Long id) {
        service.delete(id);
        return new BaseResponse<>(true, "Deleted", null);
    }
}

