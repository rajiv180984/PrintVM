package com.first.auth.security;

import com.first.auth.config.AppProperties;
import com.first.auth.model.Role;
import com.first.auth.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * Issues and verifies HMAC-SHA256 signed access tokens.
 */
@Service
public class JwtService {

    public static final String CLAIM_USERNAME = "username";
    public static final String CLAIM_EMAIL = "email";
    public static final String CLAIM_ROLES = "roles";
    public static final String CLAIM_ROLE = "role";
    public static final String CLAIM_TOKEN_VERSION = "ver";
    public static final String CLAIM_TYPE = "typ";
    private static final String TYPE_ACCESS = "access";

    private final SecretKey key;
    private final String issuer;
    private final Duration accessTokenTtl;

    public JwtService(AppProperties props) {
        AppProperties.Jwt cfg = props.jwt();
        byte[] keyBytes = Decoders.BASE64.decode(cfg.secret());
        if (keyBytes.length < 32) {
            throw new IllegalStateException("app.jwt.secret must be a Base64 encoded key of at least 256 bits");
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.issuer = cfg.issuer();
        this.accessTokenTtl = cfg.accessTokenTtl();
    }

    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuer(issuer)
                .subject(user.getId())
                .claim(CLAIM_USERNAME, user.getUsername())
                .claim(CLAIM_EMAIL, user.getEmail())
                .claim(CLAIM_ROLES, user.getRoles().stream().map(Role::name).sorted().toList())
                .claim(CLAIM_ROLE, Role.primaryOf(user.getRoles()).name())
                .claim(CLAIM_TOKEN_VERSION, user.getTokenVersion())
                .claim(CLAIM_TYPE, TYPE_ACCESS)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(accessTokenTtl)))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Verifies signature, expiry, issuer and token type.
     *
     * @throws JwtException if the token is not a valid access token
     */
    public Claims parseAccessToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .requireIssuer(issuer)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        if (!TYPE_ACCESS.equals(claims.get(CLAIM_TYPE, String.class))) {
            throw new JwtException("Token is not an access token");
        }
        return claims;
    }

    public long getAccessTokenTtlSeconds() {
        return accessTokenTtl.toSeconds();
    }
}
