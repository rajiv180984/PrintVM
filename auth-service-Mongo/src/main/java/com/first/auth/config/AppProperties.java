package com.first.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

/**
 * Typed view of the {@code app.*} section of application.yml.
 */
@ConfigurationProperties(prefix = "app")
public record AppProperties(Jwt jwt, Security security, Mail mail, Seed seed, String frontendUrl) {

    public record Jwt(String secret, String issuer, Duration accessTokenTtl, Duration refreshTokenTtl) {
    }

    public record Security(int maxFailedAttempts,
                           Duration lockDuration,
                           Duration passwordResetTtl,
                           Duration emailVerificationTtl,
                           boolean requireEmailVerification,
                           boolean exposeTokensInResponse,
                           List<String> corsAllowedOrigins) {
    }

    public record Mail(boolean enabled, String from) {
    }

    public record Seed(boolean enabled, String adminPassword, String userPassword) {
    }
}
