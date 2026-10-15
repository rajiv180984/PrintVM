package com.first.auth.security;

import com.first.auth.model.User;
import com.first.auth.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Component;

/**
 * Full access-token check: signature/expiry via {@link JwtService} plus the server-side state of the
 * user (exists, enabled, not locked, token version not revoked). Shared by the request filter and the
 * public validate-token endpoint so both apply identical rules.
 */
@Component
public class AccessTokenVerifier {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AccessTokenVerifier(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public record VerifiedToken(User user, Claims claims) {
    }

    /**
     * @throws JwtException          if the token is invalid, expired or revoked
     * @throws IllegalArgumentException if the token is empty
     */
    public VerifiedToken verify(String token) {
        Claims claims = jwtService.parseAccessToken(token);
        User user = userRepository.findById(claims.getSubject())
                .orElseThrow(() -> new JwtException("User no longer exists"));
        Integer version = claims.get(JwtService.CLAIM_TOKEN_VERSION, Integer.class);
        if (version == null || version != user.getTokenVersion()) {
            throw new JwtException("Token has been revoked");
        }
        if (!user.isEnabled()) {
            throw new JwtException("Account is disabled");
        }
        if (user.isLocked()) {
            throw new JwtException("Account is locked");
        }
        return new VerifiedToken(user, claims);
    }
}
