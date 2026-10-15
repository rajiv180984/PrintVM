package com.first.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.first.auth.model.Role;
import com.first.auth.model.User;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Response bodies returned by the API.
 */
public final class AuthResponses {

    private AuthResponses() {
    }

    public record TokenResponse(
            String accessToken,
            String refreshToken,
            String tokenType,
            long expiresIn,
            long refreshExpiresIn,
            Role role,
            UserResponse user) {
    }

    public record UserResponse(
            String id,
            String username,
            String email,
            String firstName,
            String lastName,
            Role role,
            Set<Role> roles,
            boolean enabled,
            boolean emailVerified,
            boolean locked,
            Instant lastLoginAt,
            Instant createdAt) {

        public static UserResponse from(User u) {
            return new UserResponse(u.getId(), u.getUsername(), u.getEmail(), u.getFirstName(), u.getLastName(),
                    Role.primaryOf(u.getRoles()), u.getRoles(), u.isEnabled(), u.isEmailVerified(), u.isLocked(), u.getLastLoginAt(), u.getCreatedAt());
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record RegistrationResponse(String message, UserResponse user, String debugVerificationToken) {
    }

    /**
     * Generic acknowledgement. {@code debugToken} is only populated when
     * {@code app.security.expose-tokens-in-response=true} (local testing without SMTP).
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record MessageResponse(String message, String debugToken) {

        public MessageResponse(String message) {
            this(message, null);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record TokenValidationResponse(
            boolean valid,
            String userId,
            String username,
            Role role,
            List<String> roles,
            Instant issuedAt,
            Instant expiresAt,
            String error) {

        public static TokenValidationResponse invalid(String error) {
            return new TokenValidationResponse(false, null, null, null, null, null, null, error);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ErrorResponse(
            Instant timestamp,
            int status,
            String error,
            String message,
            String path,
            Map<String, String> fieldErrors) {
    }

    public record PageResponse<T>(List<T> content, int page, int size, long totalElements, int totalPages) {
    }
}
