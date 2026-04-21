package com.example.hrtool.service.impl;

import com.example.hrtool.dto.EmployeeBlockStatusDto;
import com.example.hrtool.model.EmployeeProfile;
import com.example.hrtool.model.ProfileViewStatus;
import com.example.hrtool.model.SystemUser;
import com.example.hrtool.repository.EmployeeProfileRepository;
import com.example.hrtool.repository.ProfileViewStatusRepository;
import com.example.hrtool.repository.SystemUserRepository;
import com.example.hrtool.service.EmployeeStatusService;
import com.example.hrtool.service.ProfileViewStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileViewStatusServiceImpl implements ProfileViewStatusService {

    private final ProfileViewStatusRepository viewRepo;
    private final EmployeeProfileRepository employeeRepo;
    private final SystemUserRepository userRepo;
    private final SystemUserServiceImpl systemUserService;
    private final EmployeeStatusService employeeStatusService;

    private Long getCurrentUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("Principal: " + auth.getPrincipal());
//        System.out.println("Name: " + auth.getName());
//        System.out.println("Authorities: " + auth.getAuthorities());

        SystemUser user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        return user.getId();
    }

    public String getCurrentUserRole() {
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities()
                .stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(r -> r.replace("ROLE_", ""))   // превращает ROLE_HR → HR
                .orElse(null);
    }


    @Override
    public void markAsViewed(Long employeeId) {
        Long userId = getCurrentUserId();

        EmployeeProfile employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        SystemUser user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Найти существующую запись
        ProfileViewStatus status = viewRepo
                .findByEmployeeIdAndUserId(employeeId, userId)
                .orElse(null);

        // Если записи нет → создать новую
        if (status == null) {
            status = new ProfileViewStatus();
            status.setEmployee(employee);
            status.setUser(user);
        }

        // Пометить как просмотренный
        status.setViewed(true);
        status.setViewedAt(LocalDateTime.now());

        viewRepo.save(status);
    }


    @Override
    public int getUnreadCount() {
        return getUnreadByRole().size();
    }

    @Override
    public List<EmployeeBlockStatusDto> getUnreadByRole() {
        String role = getCurrentUserRole();

        // 1) получить список профилей по роли
        List<EmployeeBlockStatusDto> allByRole = employeeStatusService.getStatusByRole(role);

        Long userId = getCurrentUserId();

        return allByRole.stream()
                .filter(dto -> {

                    // найти ProfileViewStatus
                    Optional<ProfileViewStatus> st = viewRepo
                            .findByEmployeeIdAndUserId(dto.getId(), userId);

                    // UNREAD, если нет записи или viewed=false
                    return st.isEmpty() || !st.get().isViewed();
                })
                .collect(Collectors.toList());
    }


    @Override
    public boolean isUnread(Long employeeId) {
        Long userId = getCurrentUserId();

        return viewRepo
                .findByEmployeeIdAndUserId(employeeId, userId)
                .map(status -> !status.isViewed())
                .orElse(true);  // Если записи нет → непрочитано
    }

}
