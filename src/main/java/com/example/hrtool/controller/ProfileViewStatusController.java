package com.example.hrtool.controller;

import com.example.hrtool.dto.EmployeeBlockStatusDto;
import com.example.hrtool.service.ProfileViewStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile-view")
@RequiredArgsConstructor
public class ProfileViewStatusController {

    private final ProfileViewStatusService service;

    @PostMapping("/{employeeId}/viewed")
    public void markViewed(@PathVariable Long employeeId) {
        service.markAsViewed(employeeId);
    }

    @GetMapping("/unread-count")
    public int getUnreadCount() {
        return service.getUnreadCount();
    }

    @GetMapping("/unread")
    public List<EmployeeBlockStatusDto> getUnread() {
        return service.getUnreadByRole();
    }
}
