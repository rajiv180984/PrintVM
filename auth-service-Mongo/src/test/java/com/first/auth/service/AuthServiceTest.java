package com.first.auth.service;

import com.first.auth.config.AppProperties;
import com.first.auth.dto.AuthRequests.ChangePasswordRequest;
import com.first.auth.dto.AuthRequests.LoginRequest;
import com.first.auth.dto.AuthRequests.RegisterRequest;
import com.first.auth.dto.AuthRequests.ResetPasswordRequest;
import com.first.auth.dto.AuthResponses.MessageResponse;
import com.first.auth.dto.AuthResponses.TokenResponse;
import com.first.auth.exception.ApiException;
import com.first.auth.model.Role;
import com.first.auth.model.TokenType;
import com.first.auth.model.User;
import com.first.auth.repository.UserRepository;
import com.first.auth.security.AccessTokenVerifier;
import com.first.auth.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    private static final String PASSWORD = "Alice@12345";

    private final PasswordEncoder encoder = new BCryptPasswordEncoder(4);
    private UserRepository userRepository;
    private RefreshTokenService refreshTokenService;
    private OneTimeTokenService oneTimeTokenService;
    private EmailService emailService;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        AppProperties props = new AppProperties(
                new AppProperties.Jwt(Base64.getEncoder().encodeToString(
                        "test-secret-key-that-is-at-least-32-bytes-long".getBytes()),
                        "auth-service", Duration.ofMinutes(15), Duration.ofDays(7)),
                new AppProperties.Security(5, Duration.ofMinutes(15), Duration.ofMinutes(30), Duration.ofHours(24),
                        false, true, List.of()),
                new AppProperties.Mail(false, "no-reply@test"),
                new AppProperties.Seed(false, "x", "y"),
                "https://localhost:8443");
        userRepository = mock(UserRepository.class);
        refreshTokenService = mock(RefreshTokenService.class);
        oneTimeTokenService = mock(OneTimeTokenService.class);
        emailService = mock(EmailService.class);
        JwtService jwtService = new JwtService(props);
        authService = new AuthService(userRepository, encoder, jwtService,
                new AccessTokenVerifier(jwtService, userRepository), refreshTokenService, oneTimeTokenService,
                emailService, mock(MongoTemplate.class), props);

        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(refreshTokenService.issue(anyString(), any())).thenReturn("refresh-token");
    }

    private User alice() {
        User u = new User();
        u.setId("u1");
        u.setUsername("alice");
        u.setEmail("alice@example.com");
        u.setPassword(encoder.encode(PASSWORD));
        u.setRoles(Set.of(Role.USER));
        return u;
    }

    @Test
    void registerNormalizesAndHashesPassword() {
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId("new-id");
            return u;
        });
        when(oneTimeTokenService.create(eq("new-id"), eq(TokenType.EMAIL_VERIFICATION), any())).thenReturn("verify-tok");

        var response = authService.register(
                new RegisterRequest(" Alice ", "Alice@Example.com", PASSWORD, "Alice", null));

        assertThat(response.user().username()).isEqualTo("alice");
        assertThat(response.user().email()).isEqualTo("alice@example.com");
        assertThat(response.user().roles()).containsExactly(Role.USER);
        assertThat(response.user().role()).isEqualTo(Role.USER);
        assertThat(response.debugVerificationToken()).isEqualTo("verify-tok");
        verify(emailService).sendVerificationEmail(any(), eq(
                "https://localhost:8443/api/v1/auth/verify-email?token=verify-tok"), any());
    }

    @Test
    void registerRejectsDuplicateUsername() {
        when(userRepository.existsByUsername("alice")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(
                new RegisterRequest("alice", "a@example.com", PASSWORD, null, null)))
                .isInstanceOfSatisfying(ApiException.class, e -> assertThat(e.getStatus()).isEqualTo(HttpStatus.CONFLICT));
    }

    @Test
    void loginWithEmailReturnsTokens() {
        when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(alice()));

        TokenResponse tokens = authService.login(new LoginRequest("ALICE@example.com", PASSWORD), new ClientInfo("ip", "ua"));

        assertThat(tokens.accessToken()).isNotBlank();
        assertThat(tokens.refreshToken()).isEqualTo("refresh-token");
        assertThat(tokens.tokenType()).isEqualTo("Bearer");
        assertThat(tokens.expiresIn()).isEqualTo(900);
        assertThat(tokens.role()).isEqualTo(Role.USER);
    }

    @Test
    void loginWithUnknownUserIsUnauthorized() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(new LoginRequest("ghost", PASSWORD), new ClientInfo("ip", "ua")))
                .isInstanceOfSatisfying(ApiException.class, e -> assertThat(e.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED));
    }

    @Test
    void loginToLockedAccountIsRejectedEvenWithCorrectPassword() {
        User user = alice();
        user.setLockedUntil(Instant.now().plusSeconds(600));
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> authService.login(new LoginRequest("alice", PASSWORD), new ClientInfo("ip", "ua")))
                .isInstanceOfSatisfying(ApiException.class, e -> assertThat(e.getStatus()).isEqualTo(HttpStatus.LOCKED));
    }

    @Test
    void loginToDisabledAccountIsForbidden() {
        User user = alice();
        user.setEnabled(false);
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> authService.login(new LoginRequest("alice", PASSWORD), new ClientInfo("ip", "ua")))
                .isInstanceOfSatisfying(ApiException.class, e -> assertThat(e.getStatus()).isEqualTo(HttpStatus.FORBIDDEN));
    }

    @Test
    void forgotPasswordForUnknownEmailGivesSameMessage() {
        when(userRepository.findByEmail("nobody@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(alice()));
        when(oneTimeTokenService.create(eq("u1"), eq(TokenType.PASSWORD_RESET), any())).thenReturn("reset-tok");

        MessageResponse unknown = authService.forgotPassword("nobody@example.com");
        MessageResponse known = authService.forgotPassword("alice@example.com");

        assertThat(unknown.message()).isEqualTo(known.message());
        assertThat(unknown.debugToken()).isNull();
        assertThat(known.debugToken()).isEqualTo("reset-tok");
    }

    @Test
    void resetPasswordChangesHashRevokesSessionsAndBumpsTokenVersion() {
        User user = alice();
        user.setLockedUntil(Instant.now().plusSeconds(600));
        when(oneTimeTokenService.consume("reset-tok", TokenType.PASSWORD_RESET)).thenReturn("u1");
        when(userRepository.findById("u1")).thenReturn(Optional.of(user));

        authService.resetPassword(new ResetPasswordRequest("reset-tok", "NewAlice@123"));

        assertThat(encoder.matches("NewAlice@123", user.getPassword())).isTrue();
        assertThat(user.getTokenVersion()).isEqualTo(1);
        assertThat(user.isLocked()).isFalse();
        verify(refreshTokenService).revokeAllForUser("u1");
        verify(emailService).sendPasswordChangedEmail(user);
    }

    @Test
    void changePasswordRequiresCorrectCurrentPassword() {
        when(userRepository.findById("u1")).thenReturn(Optional.of(alice()));

        assertThatThrownBy(() -> authService.changePassword("u1",
                new ChangePasswordRequest("wrong", "NewAlice@123")))
                .isInstanceOfSatisfying(ApiException.class, e -> assertThat(e.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST));
        verify(refreshTokenService, never()).revokeAllForUser(anyString());
    }

    @Test
    void changePasswordRejectsReusingCurrentPassword() {
        when(userRepository.findById("u1")).thenReturn(Optional.of(alice()));

        assertThatThrownBy(() -> authService.changePassword("u1", new ChangePasswordRequest(PASSWORD, PASSWORD)))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("different");
    }

    @Test
    void validateTokenReportsRevokedTokenAsInvalid() {
        User user = alice();
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
        when(userRepository.findById("u1")).thenReturn(Optional.of(user));
        String token = authService.login(new LoginRequest("alice", PASSWORD), new ClientInfo("ip", "ua")).accessToken();

        assertThat(authService.validateToken(token).valid()).isTrue();

        user.incrementTokenVersion(); // what logout-all / password change does
        var result = authService.validateToken(token);
        assertThat(result.valid()).isFalse();
        assertThat(result.error()).contains("revoked");
    }
}
