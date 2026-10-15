package com.first.auth.dto;

import com.first.auth.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * Request bodies accepted by the API.
 */
public final class AuthRequests {

    private AuthRequests() {
    }

    public record RegisterRequest(
            @Schema(example = "alice")
            @NotBlank @Size(min = 3, max = 50)
            @Pattern(regexp = "^[A-Za-z0-9._-]+$", message = "Username may contain letters, digits, '.', '_' and '-' only")
            String username,

            @Schema(example = "alice@example.com")
            @NotBlank @Email @Size(max = 254)
            String email,

            @Schema(example = "Alice@12345")
            @NotBlank @Pattern(regexp = PasswordPolicy.REGEX, message = PasswordPolicy.MESSAGE)
            String password,

            @Schema(example = "Alice") @Size(max = 100) String firstName,
            @Schema(example = "Wonder") @Size(max = 100) String lastName) {
    }

    public record LoginRequest(
            @Schema(description = "Username or email", example = "alice")
            @NotBlank String usernameOrEmail,
            @Schema(example = "Alice@12345")
            @NotBlank String password) {
    }

    public record RefreshTokenRequest(@NotBlank String refreshToken) {
    }

    public record EmailRequest(
            @Schema(example = "alice@example.com")
            @NotBlank @Email String email) {
    }

    public record ResetPasswordRequest(
            @NotBlank String token,
            @Schema(example = "NewAlice@12345")
            @NotBlank @Pattern(regexp = PasswordPolicy.REGEX, message = PasswordPolicy.MESSAGE)
            String newPassword) {
    }

    public record ChangePasswordRequest(
            @NotBlank String currentPassword,
            @NotBlank @Pattern(regexp = PasswordPolicy.REGEX, message = PasswordPolicy.MESSAGE)
            String newPassword) {
    }

    public record UpdateProfileRequest(
            @Size(max = 100) String firstName,
            @Size(max = 100) String lastName) {
    }

    public record ValidateTokenRequest(@NotBlank String token) {
    }

    public record UpdateRolesRequest(@NotEmpty Set<Role> roles) {
    }

    public record UpdateStatusRequest(@NotNull Boolean enabled) {
    }
}
