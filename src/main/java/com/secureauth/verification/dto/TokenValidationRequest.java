package com.secureauth.verification.dto;

public record TokenValidationRequest(
        String tokenValue,
        String sessionId
) {} 