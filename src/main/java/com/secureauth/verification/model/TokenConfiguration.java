package com.secureauth.verification.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "token_configurations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token_length", nullable = false)
    private int tokenLength;

    @Column(name = "validity_period_minutes", nullable = false)
    private long validityPeriodMinutes;
} 