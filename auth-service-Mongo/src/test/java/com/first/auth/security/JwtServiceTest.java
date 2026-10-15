package com.first.auth.security;

import com.first.auth.config.AppProperties;
import com.first.auth.model.Role;
import com.first.auth.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Base64;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtServiceTest {

    private static final String SECRET = Base64.getEncoder()
            .encodeToString("test-secret-key-that-is-at-least-32-bytes-long".getBytes());

    static AppProperties props(String secret, Duration accessTtl) {
        return new AppProperties(
                new AppProperties.Jwt(secret, "auth-service", accessTtl, Duration.ofDays(7)),
                new AppProperties.Security(5, Duration.ofMinutes(15), Duration.ofMinutes(30), Duration.ofHours(24),
                        false, true, List.of()),
                new AppProperties.Mail(false, "no-reply@test"),
                new AppProperties.Seed(false, "x", "y"),
                "https://localhost:8443");
    }

    private static User user() {
        User u = new User();
        u.setId("user-1");
        u.setUsername("alice");
        u.setEmail("alice@example.com");
        u.setRoles(Set.of(Role.USER, Role.ADMIN));
        u.setTokenVersion(3);
        return u;
    }

    @Test
    void generatedTokenRoundTripsClaims() {
        JwtService jwt = new JwtService(props(SECRET, Duration.ofMinutes(15)));

        Claims claims = jwt.parseAccessToken(jwt.generateAccessToken(user()));

        assertThat(claims.getSubject()).isEqualTo("user-1");
        assertThat(claims.getIssuer()).isEqualTo("auth-service");
        assertThat(claims.get(JwtService.CLAIM_USERNAME, String.class)).isEqualTo("alice");
        assertThat(claims.get(JwtService.CLAIM_ROLES, List.class)).containsExactly("ADMIN", "USER");
        assertThat(claims.get(JwtService.CLAIM_ROLE, String.class)).isEqualTo("ADMIN");
        assertThat(claims.get(JwtService.CLAIM_TOKEN_VERSION, Integer.class)).isEqualTo(3);
        assertThat(jwt.getAccessTokenTtlSeconds()).isEqualTo(900);
    }

    @Test
    void primaryRoleIsAdminOnlyWhenHeld() {
        assertThat(Role.primaryOf(Set.of(Role.USER, Role.ADMIN))).isEqualTo(Role.ADMIN);
        assertThat(Role.primaryOf(Set.of(Role.ADMIN))).isEqualTo(Role.ADMIN);
        assertThat(Role.primaryOf(Set.of(Role.USER))).isEqualTo(Role.USER);
        assertThat(Role.primaryOf(Set.of())).isEqualTo(Role.USER);
    }

    @Test
    void expiredTokenIsRejected() {
        JwtService jwt = new JwtService(props(SECRET, Duration.ofSeconds(-1)));
        String token = jwt.generateAccessToken(user());

        assertThatThrownBy(() -> jwt.parseAccessToken(token)).isInstanceOf(ExpiredJwtException.class);
    }

    @Test
    void tokenSignedWithDifferentKeyIsRejected() {
        String otherSecret = Base64.getEncoder()
                .encodeToString("another-secret-key-that-is-at-least-32-bytes".getBytes());
        String token = new JwtService(props(otherSecret, Duration.ofMinutes(5))).generateAccessToken(user());

        JwtService jwt = new JwtService(props(SECRET, Duration.ofMinutes(5)));
        assertThatThrownBy(() -> jwt.parseAccessToken(token)).isInstanceOf(JwtException.class);
    }

    @Test
    void tamperedTokenIsRejected() {
        JwtService jwt = new JwtService(props(SECRET, Duration.ofMinutes(5)));
        String token = jwt.generateAccessToken(user());
        String tampered = token.substring(0, token.length() - 2) + (token.endsWith("A") ? "BB" : "AA");

        assertThatThrownBy(() -> jwt.parseAccessToken(tampered)).isInstanceOf(JwtException.class);
    }

    @Test
    void shortSecretFailsFast() {
        String shortSecret = Base64.getEncoder().encodeToString("too-short".getBytes());

        assertThatThrownBy(() -> new JwtService(props(shortSecret, Duration.ofMinutes(5))))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void secureTokensAreRandomAndHashDeterministically() {
        String a = SecureTokens.generate();
        String b = SecureTokens.generate();

        assertThat(a).isNotEqualTo(b).hasSize(43);
        assertThat(SecureTokens.hash(a)).isEqualTo(SecureTokens.hash(a)).hasSize(64).isNotEqualTo(a);
    }
}
