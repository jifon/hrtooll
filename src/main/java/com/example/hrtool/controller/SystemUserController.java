package com.example.hrtool.controller;

import com.example.hrtool.dto.ChangePasswordDto;
import com.example.hrtool.dto.SystemUserDto;
import com.example.hrtool.dto.UserNameEmailDto;
import com.example.hrtool.payload.BaseResponse;
import com.example.hrtool.repository.SystemUserRepository;
import com.example.hrtool.security.JwtService;
import com.example.hrtool.service.SystemUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/system-users")
@RequiredArgsConstructor
public class SystemUserController {

    private final SystemUserRepository systemUserRepository;

    private final SystemUserService service;
    private final JwtService jwtService;

    @GetMapping("/{id}")
    public ResponseEntity<UserNameEmailDto> getNameAndEmail(@PathVariable Long id) {
        return service.getUserNameAndEmail(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping
    public BaseResponse<List<SystemUserDto>> getAll() {
        List<SystemUserDto> users = service.getAllUsers();
        return new BaseResponse<>(true, "OK", users);
    }

    @GetMapping("/me")
    public ResponseEntity<SystemUserDto> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(service.getCurrentUser(userDetails.getUsername()));
    }

    @PutMapping("/me")
    public ResponseEntity<SystemUserDto> updateCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody SystemUserDto dto
    ) {
        return ResponseEntity.ok(service.updateCurrentUser(userDetails.getUsername(), dto));
    }

    @PatchMapping("/me")
    public ResponseEntity<?> updateOwnProfile(@RequestBody SystemUserDto dto, Principal principal) {
        boolean updated = service.updateOwnProfile(principal.getName(), dto);
        if (updated) {
            return ResponseEntity.ok(new BaseResponse<>(true, "Profil aktualisiert", null));
        } else {
            return ResponseEntity.status(400).body(new BaseResponse<>(false, "Fehler beim Speichern", null));
        }
    }

    @PutMapping("/changePassword")
    public BaseResponse<?> changePassword(@RequestBody ChangePasswordDto dto, @RequestHeader("Authorization") String authHeader) {
        try{
            String jwt = authHeader.substring(7);
            Long tokenId = jwtService.extractSystemUserId(jwt);
            service.changePassword(dto, tokenId);
            return new BaseResponse<>(true, "OK", null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/numOfUsers")
    public BaseResponse<Long> numOfUsers(){
        long users = systemUserRepository.count();
        return new BaseResponse<Long>(true, "OK", users);
    }

}
