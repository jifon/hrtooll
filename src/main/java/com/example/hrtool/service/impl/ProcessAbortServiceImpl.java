package com.example.hrtool.service.impl;

import com.example.hrtool.exception.ModelNotFoundException;
import com.example.hrtool.model.*;
import com.example.hrtool.model.enums.AbortState;
import com.example.hrtool.model.enums.ProcessStep;
import com.example.hrtool.repository.*;
import com.example.hrtool.service.ProcessAbortService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class ProcessAbortServiceImpl implements ProcessAbortService {

    private final EmployeeProfileRepository repository;


    @Override
    public void startAbort(Long employeeId, SystemUser.Role initiator) {

        if (initiator != SystemUser.Role.HR
                && initiator != SystemUser.Role.RECRUITING
                && initiator != SystemUser.Role.BEREICHSLEITER) {

            throw new IllegalStateException("Role not allowed to start abort.");
        }

        EmployeeProfile e = repository.findById(employeeId)
                .orElseThrow(() -> new IllegalStateException("Employee not found"));

        if (e.getAbortState() != AbortState.NONE) {
            throw new IllegalStateException("Abort already running.");
        }

        e.setAbortState(AbortState.IN_PROGRESS);
        e.setAbortInitiatedAt(LocalDateTime.now());

        // SNAPSHOT required roles
        Set<SystemUser.Role> required = determineRequiredRoles(e);

        e.setAbortRequiredRoles(required);

        Set<SystemUser.Role> confirmed = new HashSet<>();
        confirmed.add(initiator);
        e.setAbortConfirmedRoles(confirmed);

        repository.save(e);
    }

    @Override
    public void confirmAbort(Long employeeId, SystemUser.Role role) {

        EmployeeProfile e = repository.findById(employeeId)
                .orElseThrow(() -> new IllegalStateException("Employee not found"));

        if (e.getAbortState() != AbortState.IN_PROGRESS) {
            throw new IllegalStateException("Abort not active.");
        }

        if (!e.getAbortRequiredRoles().contains(role)) {
            throw new IllegalStateException("Role not required to confirm abort.");
        }

        e.getAbortConfirmedRoles().add(role);

        // 🔥 если все подтвердили → финализируем
        if (e.getAbortConfirmedRoles()
                .containsAll(e.getAbortRequiredRoles())) {

            finalizeAbort(e);
        }

        repository.save(e);
    }

    // =====================================================
    // 🔎 Определяем какие роли должны подтвердить
    // =====================================================


    private Set<SystemUser.Role> determineRequiredRoles(EmployeeProfile e) {

        Set<SystemUser.Role> roles = new HashSet<>();

        if (e.isRecruitingCompleted()) roles.add(SystemUser.Role.RECRUITING);
        if (e.isBereichsleiterCompleted()) roles.add(SystemUser.Role.BEREICHSLEITER);
        if (e.isHrCompleted()) roles.add(SystemUser.Role.HR);
        if (e.isKlCompleted()) roles.add(SystemUser.Role.KL);
        if (e.isItCompleted()) roles.add(SystemUser.Role.IT);

        return roles;
    }


    // =====================================================
    // 🏁 Финализация
    // =====================================================

    private void finalizeAbort(EmployeeProfile e) {

        e.setAbortState(AbortState.DONE);
        e.setProcessingAborted(true);

        e.getActiveProcessSteps().clear();
        e.getActiveProcessSteps().add(ProcessStep.ABORTED);
    }




}
