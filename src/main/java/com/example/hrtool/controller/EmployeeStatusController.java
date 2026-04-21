package com.example.hrtool.controller;

import com.example.hrtool.dto.EmployeeBlockStatusDto;
import com.example.hrtool.dto.SidebarSimpleEmployeeDto;
import com.example.hrtool.model.SystemUser;
import com.example.hrtool.model.enums.AbortState;
import com.example.hrtool.service.EmployeeStatusService;
import com.example.hrtool.service.NextProcessStepUseCase;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class EmployeeStatusController {

    private final EmployeeStatusService service;
    private final NextProcessStepUseCase nextProcessStepUseCase;

    //ошибка
    @GetMapping("/archived")
    public List<EmployeeBlockStatusDto> getArchived() {
        return service.getArchived();
    }

    @GetMapping("/dashboard")
    public List<EmployeeBlockStatusDto> dashboard(Authentication auth) {
        String roleUpper = extractRole(auth); // HR / RECRUITING / BEREICHSLEITER / IT / KL

        List<EmployeeBlockStatusDto> list = service.getAllStatus();

        for (EmployeeBlockStatusDto dto : list) {

            dto.setAbortState(dto.getAbortState()); // просто пробрасываем

            boolean canStart = dto.getAbortState() == AbortState.NONE
                    && (roleUpper.equals("HR")
                    || roleUpper.equals("RECRUITING")
                    || roleUpper.equals("BEREICHSLEITER"));

            boolean canConfirm = dto.getAbortState() == AbortState.IN_PROGRESS
                    && dto.getAbortRequiredRoles() != null
                    && dto.getAbortRequiredRoles().contains(roleUpper)
                    && (dto.getAbortConfirmedRoles() == null
                    || !dto.getAbortConfirmedRoles().contains(roleUpper));

            dto.setCanStartAbort(canStart);
            dto.setCanConfirmAbort(canConfirm);
        }

        return list;
    }

    private String extractRole(Authentication auth) {
        return auth.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .orElseThrow();
    }




    @Operation(summary = "Get statuses from related to me", description = "Returns all statuses of employees.")
    @GetMapping
    public ResponseEntity<List<EmployeeBlockStatusDto>> getEmployeeStatusesByRole(Authentication authentication) {
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(r -> r.replace("ROLE_", "")) // Превращает "ROLE_HR" → "HR"
                .orElse(null);

        List<EmployeeBlockStatusDto> result = service.getStatusByRole(role);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/filter")
    public List<EmployeeBlockStatusDto> filterByBlockStatus(
            @RequestParam(required = false) Boolean personalData,
            @RequestParam(required = false) Boolean companyData,
            @RequestParam(required = false) Boolean qualificationData,
            @RequestParam(required = false) Boolean financeData,
            @RequestParam(required = false) Boolean inventoryData,
            @RequestParam(required = false) Boolean accessData,
            @RequestParam(required = false) Boolean sapNumber
    ) {
        return service.getFilteredStatus(personalData, companyData, qualificationData, financeData, inventoryData, accessData, sapNumber);
    }


    @GetMapping("/search")
    public List<EmployeeBlockStatusDto> searchByName(@RequestParam String name) {
        return service.getAllStatus().stream()
                .filter(dto -> dto.getFullName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    @Operation(summary = "Get all statuses", description = "Returns all statuses of employees.")
    @GetMapping("/advanced")
    public List<EmployeeBlockStatusDto> getAdvancedFilteredStatus(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean personalData,
            @RequestParam(required = false) Boolean companyData,
            @RequestParam(required = false) Boolean qualificationData,
            @RequestParam(required = false) Boolean financeData,
            @RequestParam(required = false) Boolean inventoryData,
            @RequestParam(required = false) Boolean accessData,
            @RequestParam(required = false) Boolean sapNumber
    ) {
        return service.getAdvancedFilteredStatus(role, name, personalData, companyData, qualificationData,
                financeData, inventoryData, accessData, sapNumber);
    }


    @PostMapping("/process/{id}/next")
    public void nextStep(@PathVariable Long id, Authentication auth) {

        SystemUser.Role role = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(r -> r.replace("ROLE_", ""))
                .map(SystemUser.Role::valueOf)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Role not found"));

        nextProcessStepUseCase.proceed(id, role);
    }

    @GetMapping("/last-processed")
    public List<SidebarSimpleEmployeeDto> getLastProcessedForMyRole() {
        return service.getLastProcessedForMyRole();
    }


}

