package com.example.hrtool.controller;

import com.example.hrtool.dto.EmployeeProfileDto;
import com.example.hrtool.dto.SimpleEmployeeDto;
import com.example.hrtool.model.EmployeeProfile;
import com.example.hrtool.payload.BaseResponse;
import com.example.hrtool.service.EmployeeProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeProfileController {

    private final EmployeeProfileService service;

    @PostMapping("/create-with-name")
    public BaseResponse<Long> createWithName(@RequestBody Map<String, String> payload) {
        String firstName = payload.get("firstName");
        String lastName = payload.get("lastName");
        Long newId = service.createWithNameAndSurname(firstName, lastName);
        return new BaseResponse<>(true, "Created", newId);
    }

    @PostMapping("/{employeeId}/start/")
    public ResponseEntity<Void> startProcessing(@PathVariable Long employeeId) {
        service.startProcessing(employeeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/aborted/all")
    public ResponseEntity<List<SimpleEmployeeDto>> getAllAbortedProfiles() {
        return ResponseEntity.ok(service.getAllAbortedProfiles());
    }

    @GetMapping
    public BaseResponse<List<EmployeeProfileDto>> getAll() {
        return new BaseResponse<>(true, "OK", service.getAll());
    }

    @GetMapping("/{id}")
    public BaseResponse<EmployeeProfileDto> get(@PathVariable Long id,
                                                Authentication auth) {
        String role = auth.getAuthorities().stream()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .findFirst()
                .orElse(null);

        return new BaseResponse<>(true, "OK", service.getById(id, role));
    }

    @PostMapping
    public BaseResponse<EmployeeProfileDto> create(@RequestBody EmployeeProfileDto dto) {
        return new BaseResponse<>(true, "Created", service.create(dto));
    }

    @PutMapping("/{id}")
    public BaseResponse<EmployeeProfileDto> update(@PathVariable Long id, @RequestBody EmployeeProfileDto dto) {
        return new BaseResponse<>(true, "Updated", service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public BaseResponse<String> delete(@PathVariable Long id) {
        service.delete(id);
        return new BaseResponse<>(true, "Deleted", null);
    }

    @GetMapping("/filter")
    public BaseResponse<List<EmployeeProfileDto>> filter(@RequestParam(required = false) String status,
                                                         @RequestParam(required = false) String role,
                                                         @RequestParam(required = false) String name) {
        return new BaseResponse<>(true, "OK", service.filter(status, role, name));
    }

}
