package com.secureauth.verification.controller;

import com.secureauth.verification.dto.TokenGenerationRequest;
import com.secureauth.verification.dto.TokenValidationRequest;
import com.secureauth.verification.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/tokens")
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateToken(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody TokenGenerationRequest request) {
        if (request.sessionId() == null || request.sessionId().isBlank()) {
            return ResponseEntity.badRequest().body("Session ID is required");
        }
        if (request.deliveryMethod() == null) {
            return ResponseEntity.badRequest().body("Delivery method must be specified");
        }

        String tokenValue = tokenService.generateToken(
                user.getUsername(),
                request.sessionId(),
                request.deliveryMethod()
        );

        return ResponseEntity.ok(Map.of(
                "tokenValue", tokenValue
        ));
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody TokenValidationRequest request) {
        if (request.tokenValue() == null || request.tokenValue().isBlank()) {
            return ResponseEntity.badRequest().body("Token value is required");
        }
        if (request.sessionId() == null || request.sessionId().isBlank()) {
            return ResponseEntity.badRequest().body("Session ID is required");
        }

        boolean isValid = tokenService.validateToken(
                user.getUsername(),
                request.tokenValue(),
                request.sessionId()
        );

        return ResponseEntity.ok(Map.of(
                "valid", isValid
        ));
    }
} 