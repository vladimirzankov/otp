package com.secureauth.verification.controller;

import com.secureauth.verification.model.TokenConfiguration;
import com.secureauth.verification.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @PutMapping("/config")
    public ResponseEntity<?> updateTokenConfiguration(@RequestBody TokenConfiguration config) {
        adminService.updateTokenConfiguration(config);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/config")
    public ResponseEntity<?> getTokenConfiguration() {
        return ResponseEntity.ok(adminService.getTokenConfiguration());
    }
} 