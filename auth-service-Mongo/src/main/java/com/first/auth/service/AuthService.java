package com.first.auth.service;

import com.first.auth.config.AppProperties;
import com.first.auth.dto.AuthRequests.ChangePasswordRequest;
import com.first.auth.dto.AuthRequests.LoginRequest;
import com.first.auth.dto.AuthRequests.RegisterRequest;
import com.first.auth.dto.AuthRequests.ResetPasswordRequest;
import com.first.auth.dto.AuthResponses.MessageResponse;
import com.first.auth.dto.AuthResponses.RegistrationResponse;
import com.first.auth.dto.AuthResponses.TokenResponse;
import com.first.auth.dto.AuthResponses.TokenValidationResponse;
import com.first.auth.dto.AuthResponses.UserResponse;
import com.first.auth.exception.ApiException;
import com.first.auth.model.RefreshToken;
import com.first.auth.model.Role;
import com.first.auth.model.TokenType;
import com.first.auth.model.User;
import com.first.auth.repository.UserRepository;
import com.first.auth.security.AccessTokenVerifier;
import com.first.auth.security.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private static final String GENERIC_RESET_MESSAGE =
            "If an account exists for that email, a password reset link has been sent.";
    private static final String GENERIC_VERIFY_MESSAGE =
            "If the account exists and is not yet verified, a verification link has been sent.";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AccessTokenVerifier accessTokenVerifier;
    private final RefreshTokenService refreshTokenService;
    private final OneTimeTokenService oneTimeTokenService;
    private final EmailService emailService;
    private final MongoTemplate mongoTemplate;
    private final AppProperties props;

    /** Compared against when the user does not exist, so response time does not reveal valid usernames. */
    private final String dummyPasswordHash;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
                       AccessTokenVerifier accessTokenVerifier, RefreshTokenService refreshTokenService,
                       OneTimeTokenService oneTimeTokenService, EmailService emailService,
                       MongoTemplate mongoTemplate, AppProperties props) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.accessTokenVerifier = accessTokenVerifier;
        this.refreshTokenService = refreshTokenService;
        this.oneTimeTokenService = oneTimeTokenService;
        this.emailService = emailService;
        this.mongoTemplate = mongoTemplate;
        this.props = props;
        this.dummyPasswordHash = passwordEncoder.encode("dummy-password-for-timing");
    }

    public RegistrationResponse register(RegisterRequest req) {
        String username = normalize(req.username());
        String email = normalize(req.email());
        if (userRepository.existsByUsername(username)) {
            throw ApiException.conflict("Username is already taken");
        }
        if (userRepository.existsByEmail(email)) {
            throw ApiException.conflict("Email is already registered");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setFirstName(trimToNull(req.firstName()));
        user.setLastName(trimToNull(req.lastName()));
        user.setRoles(Set.of(Role.USER));
        user.setPasswordChangedAt(Instant.now());
        user = userRepository.save(user); // unique indexes still guard against a concurrent duplicate

        String verificationToken = sendVerification(user);
        log.info("Registered user {} ({})", user.getUsername(), user.getId());
        return new RegistrationResponse("User registered. Please verify your email address.",
                UserResponse.from(user), exposeToken(verificationToken));
    }

    public TokenResponse login(LoginRequest req, ClientInfo client) {
        String key = normalize(req.usernameOrEmail());
        // Usernames cannot contain '@', so this unambiguously identifies email logins.
        User user = (key.contains("@") ? userRepository.findByEmail(key) : userRepository.findByUsername(key))
                .orElse(null);

        if (user == null) {
            passwordEncoder.matches(req.password(), dummyPasswordHash);
            throw ApiException.unauthorized("Invalid username/email or password");
        }
        if (user.isLocked()) {
            throw new ApiException(HttpStatus.LOCKED,
                    "Account is temporarily locked due to too many failed attempts. Try again after " + user.getLockedUntil());
        }
        if (!passwordEncoder.matches(req.password(), user.getPassword())) {
            recordFailedLogin(user);
            throw ApiException.unauthorized("Invalid username/email or password");
        }
        if (!user.isEnabled()) {
            throw ApiException.forbidden("Account is disabled");
        }
        if (props.security().requireEmailVerification() && !user.isEmailVerified()) {
            throw ApiException.forbidden("Email address has not been verified");
        }

        user.setFailedLoginAttempts(0);
        user.setLockedUntil(null);
        user.setLastLoginAt(Instant.now());
        user = userRepository.save(user);
        return issueTokens(user, client);
    }

    public TokenResponse refresh(String rawRefreshToken, ClientInfo client) {
        RefreshToken consumed = refreshTokenService.consume(rawRefreshToken);
        User user = userRepository.findById(consumed.getUserId())
                .orElseThrow(() -> ApiException.unauthorized("Invalid refresh token"));
        if (!user.isEnabled()) {
            throw ApiException.forbidden("Account is disabled");
        }
        if (user.isLocked()) {
            throw new ApiException(HttpStatus.LOCKED, "Account is temporarily locked");
        }
        TokenResponse tokens = issueTokens(user, client);
        refreshTokenService.markReplaced(consumed.getId(), tokens.refreshToken());
        return tokens;
    }

    public void logout(String rawRefreshToken) {
        refreshTokenService.revoke(rawRefreshToken);
    }

    /** Signs the user out everywhere: revokes all refresh tokens and invalidates outstanding access tokens. */
    public void logoutAll(String userId) {
        User user = findUser(userId);
        user.incrementTokenVersion();
        userRepository.save(user);
        refreshTokenService.revokeAllForUser(userId);
    }

    public MessageResponse forgotPassword(String email) {
        String token = userRepository.findByEmail(normalize(email))
                .filter(User::isEnabled)
                .map(user -> {
                    var ttl = props.security().passwordResetTtl();
                    String raw = oneTimeTokenService.create(user.getId(), TokenType.PASSWORD_RESET, ttl);
                    emailService.sendPasswordResetEmail(user, link("/reset-password", raw), ttl);
                    return raw;
                })
                .orElse(null);
        // Same response whether or not the email exists, to prevent account enumeration.
        return new MessageResponse(GENERIC_RESET_MESSAGE, exposeToken(token));
    }

    public void resetPassword(ResetPasswordRequest req) {
        String userId = oneTimeTokenService.consume(req.token(), TokenType.PASSWORD_RESET);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ApiException.badRequest("Invalid or expired token"));
        applyNewPassword(user, req.newPassword());
        user.setFailedLoginAttempts(0);
        user.setLockedUntil(null);
        user.setEmailVerified(true); // receiving the reset email proves ownership of the address
        userRepository.save(user);
        refreshTokenService.revokeAllForUser(userId);
        emailService.sendPasswordChangedEmail(user);
        log.info("Password reset for user {}", userId);
    }

    public void changePassword(String userId, ChangePasswordRequest req) {
        User user = findUser(userId);
        if (!passwordEncoder.matches(req.currentPassword(), user.getPassword())) {
            throw ApiException.badRequest("Current password is incorrect");
        }
        applyNewPassword(user, req.newPassword());
        userRepository.save(user);
        refreshTokenService.revokeAllForUser(userId);
        emailService.sendPasswordChangedEmail(user);
    }

    public void verifyEmail(String token) {
        String userId = oneTimeTokenService.consume(token, TokenType.EMAIL_VERIFICATION);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ApiException.badRequest("Invalid or expired token"));
        user.setEmailVerified(true);
        userRepository.save(user);
    }

    public MessageResponse resendVerification(String email) {
        String token = userRepository.findByEmail(normalize(email))
                .filter(u -> !u.isEmailVerified() && u.isEnabled())
                .map(this::sendVerification)
                .orElse(null);
        return new MessageResponse(GENERIC_VERIFY_MESSAGE, exposeToken(token));
    }

    /** Token introspection for other services that do not hold the signing key. */
    public TokenValidationResponse validateToken(String token) {
        try {
            AccessTokenVerifier.VerifiedToken verified = accessTokenVerifier.verify(token);
            Claims c = verified.claims();
            @SuppressWarnings("unchecked")
            List<String> roles = c.get(JwtService.CLAIM_ROLES, List.class);
            Role role = Role.primaryOf(roles == null ? null : roles.stream().map(Role::valueOf).toList());
            return new TokenValidationResponse(true, c.getSubject(), verified.user().getUsername(), role, roles,
                    c.getIssuedAt().toInstant(), c.getExpiration().toInstant(), null);
        } catch (ExpiredJwtException e) {
            return TokenValidationResponse.invalid("Token has expired");
        } catch (JwtException | IllegalArgumentException e) {
            return TokenValidationResponse.invalid(e.getMessage());
        }
    }

    private TokenResponse issueTokens(User user, ClientInfo client) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = refreshTokenService.issue(user.getId(), client);
        return new TokenResponse(accessToken, refreshToken, "Bearer", jwtService.getAccessTokenTtlSeconds(),
                refreshTokenService.getTtlSeconds(), Role.primaryOf(user.getRoles()), UserResponse.from(user));
    }

    private void applyNewPassword(User user, String newPassword) {
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw ApiException.badRequest("New password must be different from the current password");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordChangedAt(Instant.now());
        user.incrementTokenVersion(); // invalidates every access token issued with the old password
    }

    /** Atomic increment so concurrent failed attempts are all counted. */
    private void recordFailedLogin(User user) {
        Query byId = new Query(where("_id").is(user.getId()));
        User updated = mongoTemplate.findAndModify(byId, new Update().inc("failedLoginAttempts", 1),
                FindAndModifyOptions.options().returnNew(true), User.class);
        if (updated != null && updated.getFailedLoginAttempts() >= props.security().maxFailedAttempts()) {
            Instant until = Instant.now().plus(props.security().lockDuration());
            mongoTemplate.updateFirst(byId,
                    new Update().set("lockedUntil", until).set("failedLoginAttempts", 0), User.class);
            log.warn("User {} locked until {} after repeated failed logins", user.getId(), until);
        }
    }

    private String sendVerification(User user) {
        var ttl = props.security().emailVerificationTtl();
        String raw = oneTimeTokenService.create(user.getId(), TokenType.EMAIL_VERIFICATION, ttl);
        emailService.sendVerificationEmail(user, link("/api/v1/auth/verify-email", raw), ttl);
        return raw;
    }

    private String link(String path, String token) {
        return props.frontendUrl() + path + "?token=" + token;
    }

    private String exposeToken(String token) {
        return props.security().exposeTokensInResponse() ? token : null;
    }

    private User findUser(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> ApiException.notFound("User not found"));
    }

    static String normalize(String value) {
        return value.trim().toLowerCase(Locale.ROOT);
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
