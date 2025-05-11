package com.secureauth.verification.repository;

import com.secureauth.verification.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByTokenValueAndUserIdentifierAndSessionId(
            String tokenValue, 
            String userIdentifier, 
            String sessionId
    );

    void deleteAllByUserIdentifier(String userIdentifier);
} 