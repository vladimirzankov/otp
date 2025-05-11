package com.secureauth.verification.config;

import com.secureauth.verification.model.TokenConfiguration;
import com.secureauth.verification.repository.TokenConfigurationRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConfigurationInitializer {
    private final TokenConfigurationRepository configRepository;

    @PostConstruct
    public void init() {
        if (configRepository.findTopByOrderById().isEmpty()) {
            configRepository.save(new TokenConfiguration(null, 6, 5));
        }
    }
} 