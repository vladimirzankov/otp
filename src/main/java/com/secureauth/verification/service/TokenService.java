package com.secureauth.verification.service;

import com.secureauth.verification.dto.TokenGenerationRequest;
import com.secureauth.verification.model.TokenConfiguration;
import com.secureauth.verification.model.VerificationToken;
import com.secureauth.verification.repository.TokenConfigurationRepository;
import com.secureauth.verification.repository.VerificationTokenRepository;
import com.secureauth.verification.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final VerificationTokenRepository tokenRepository;
    private final TokenConfigurationRepository configRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    private final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public String generateToken(String userIdentifier, String sessionId, TokenGenerationRequest.DeliveryMethod deliveryMethod) {
        TokenConfiguration config = configRepository.findTopByOrderById()
                .orElseThrow(() -> new RuntimeException("Token configuration not found"));

        String tokenValue = generateSecureToken(config.getTokenLength());
        LocalDateTime now = LocalDateTime.now();

        VerificationToken token = new VerificationToken();
        token.setTokenValue(tokenValue);
        token.setUserIdentifier(userIdentifier);
        token.setSessionId(sessionId);
        token.setTokenState(VerificationToken.TokenState.ACTIVE);
        token.setCreatedAt(now);
        token.setExpiresAt(now.plusMinutes(config.getValidityPeriodMinutes()));

        tokenRepository.save(token);

        var user = userRepository.findByLogin(userIdentifier)
                .orElseThrow(() -> new RuntimeException("User not found"));

        switch (deliveryMethod) {
            case EMAIL -> notificationService.sendEmailNotification(user.getEmail(), tokenValue, sessionId);
            case TELEGRAM -> notificationService.sendTelegramNotification(user.getTelegramChatId(), tokenValue, sessionId);
            case SMS -> {
                if (user.getPhoneNumber() == null || user.getPhoneNumber().isBlank()) {
                    throw new IllegalStateException("User phone number is not set");
                }
                notificationService.sendSmsNotification(user.getPhoneNumber(), tokenValue, sessionId);
            }
            case FILE -> saveTokenToFile(tokenValue, sessionId);
        }

        return tokenValue;
    }

    @Transactional
    public boolean validateToken(String userIdentifier, String tokenValue, String sessionId) {
        Optional<VerificationToken> tokenOptional = tokenRepository
                .findByTokenValueAndUserIdentifierAndSessionId(tokenValue, userIdentifier, sessionId);

        if (tokenOptional.isEmpty()) return false;

        VerificationToken token = tokenOptional.get();

        if (token.getTokenState() != VerificationToken.TokenState.ACTIVE) return false;
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            token.setTokenState(VerificationToken.TokenState.EXPIRED);
            tokenRepository.save(token);
            return false;
        }

        token.setTokenState(VerificationToken.TokenState.CONSUMED);
        tokenRepository.save(token);
        return true;
    }

    private String generateSecureToken(int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(secureRandom.nextInt(10));
        }
        return builder.toString();
    }

    private void saveTokenToFile(String tokenValue, String sessionId) {
        try {
            String filename = "verification_token_" + sessionId + ".txt";
            String content = "Verification Token: " + tokenValue;

            Files.writeString(Path.of(filename), content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save token to file", e);
        }
    }
} 