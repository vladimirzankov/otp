package com.secureauth.verification.dto;

public record RegisterRequest(
        String login,
        String password,
        String email,
        String phoneNumber
) {} 