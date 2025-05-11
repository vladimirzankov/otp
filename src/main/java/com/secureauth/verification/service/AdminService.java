package com.secureauth.verification.service;

import com.secureauth.verification.model.TokenConfiguration;
import com.secureauth.verification.repository.TokenConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final TokenConfigurationRepository configRepository;

    @Transactional
    public void updateTokenConfiguration(TokenConfiguration config) {
        TokenConfiguration existingConfig = configRepository.findTopByOrderById()
                .orElse(new TokenConfiguration());
        
        existingConfig.setTokenLength(config.getTokenLength());
        existingConfig.setValidityPeriodMinutes(config.getValidityPeriodMinutes());
        
        configRepository.save(existingConfig);
    }

    public TokenConfiguration getTokenConfiguration() {
        return configRepository.findTopByOrderById()
                .orElseThrow(() -> new RuntimeException("Token configuration not found"));
    }
} 