package com.secureauth.verification.dto;

public record LoginRequest(
        String login,
        String password
) {} 