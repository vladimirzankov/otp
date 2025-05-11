package com.secureauth.verification.dto;

public record TokenGenerationRequest(
        String sessionId,
        DeliveryMethod deliveryMethod
) {
    public enum DeliveryMethod {
        EMAIL, TELEGRAM, SMS, FILE
    }
} 