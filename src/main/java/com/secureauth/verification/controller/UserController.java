package com.secureauth.verification.controller;

import com.secureauth.verification.dto.PhoneNumberUpdateRequest;
import com.secureauth.verification.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/phone")
    public ResponseEntity<?> updatePhoneNumber(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody PhoneNumberUpdateRequest request) {
        userService.updatePhoneNumber(user.getUsername(), request.phoneNumber());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(userService.getUserProfile(user.getUsername()));
    }
} 