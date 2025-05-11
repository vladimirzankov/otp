package com.secureauth.verification.repository;

import com.secureauth.verification.model.TokenConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenConfigurationRepository extends JpaRepository<TokenConfiguration, Long> {
    Optional<TokenConfiguration> findTopByOrderById();
} 