package com.example.hrtool.controller;

import com.example.hrtool.model.SystemUser;
import com.example.hrtool.service.ProcessAbortService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/process")
@RequiredArgsConstructor
public class ProcessAbortController {

    private final ProcessAbortService abortService;


    @PostMapping("/{id}/abort")
    public void startAbort(@PathVariable Long id,
                           Authentication auth) {

        SystemUser.Role role = extractRole(auth);
        abortService.startAbort(id, role);
    }

    @PostMapping("/{id}/abort/confirm")
    public void confirmAbort(@PathVariable Long id,
                             Authentication auth) {

        SystemUser.Role role = extractRole(auth);
        abortService.confirmAbort(id, role);
    }

    private SystemUser.Role extractRole(Authentication auth) {
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(r -> r.replace("ROLE_", ""))
                .map(SystemUser.Role::valueOf)
                .findFirst()
                .orElseThrow();
    }
}

