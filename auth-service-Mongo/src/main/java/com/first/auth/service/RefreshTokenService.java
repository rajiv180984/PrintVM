package com.first.auth.service;

import com.first.auth.config.AppProperties;
import com.first.auth.exception.ApiException;
import com.first.auth.model.RefreshToken;
import com.first.auth.repository.RefreshTokenRepository;
import com.first.auth.security.SecureTokens;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Opaque refresh tokens with rotation and reuse detection.
 * <p>
 * Every refresh consumes the presented token and issues a new one. If a token that was already
 * rotated is presented again, it has most likely been stolen, so every refresh token of that user
 * is revoked and they must log in again.
 */
@Service
public class RefreshTokenService {

    private static final Logger log = LoggerFactory.getLogger(RefreshTokenService.class);

    private final RefreshTokenRepository repository;
    private final MongoTemplate mongoTemplate;
    private final Duration ttl;

    public RefreshTokenService(RefreshTokenRepository repository, MongoTemplate mongoTemplate, AppProperties props) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
        this.ttl = props.jwt().refreshTokenTtl();
    }

    /** Creates and stores a new refresh token; returns the raw value (only ever sent to the client). */
    public String issue(String userId, ClientInfo client) {
        String raw = SecureTokens.generate();
        Instant now = Instant.now();
        RefreshToken token = new RefreshToken();
        token.setTokenHash(SecureTokens.hash(raw));
        token.setUserId(userId);
        token.setCreatedAt(now);
        token.setExpiresAt(now.plus(ttl));
        token.setCreatedByIp(client.ipAddress());
        token.setUserAgent(client.userAgent());
        repository.save(token);
        return raw;
    }

    /**
     * Atomically revokes the presented token so it can be exchanged exactly once.
     *
     * @return the consumed token record
     * @throws ApiException 401 if the token is unknown, already used, or expired
     */
    public RefreshToken consume(String rawToken) {
        String hash = SecureTokens.hash(rawToken);
        Instant now = Instant.now();
        Query activeToken = new Query(where("tokenHash").is(hash).and("revoked").is(false));
        RefreshToken token = mongoTemplate.findAndModify(activeToken,
                new Update().set("revoked", true).set("revokedAt", now), RefreshToken.class);

        if (token == null) {
            repository.findByTokenHash(hash).ifPresent(reused -> {
                if (reused.getReplacedByHash() != null) {
                    log.warn("Refresh token reuse detected for user {}; revoking all sessions", reused.getUserId());
                    revokeAllForUser(reused.getUserId());
                }
            });
            throw ApiException.unauthorized("Invalid refresh token");
        }
        if (token.isExpired()) {
            throw ApiException.unauthorized("Refresh token has expired");
        }
        return token;
    }

    public void markReplaced(String tokenId, String newRawToken) {
        mongoTemplate.updateFirst(new Query(where("_id").is(tokenId)),
                new Update().set("replacedByHash", SecureTokens.hash(newRawToken)), RefreshToken.class);
    }

    /** Logout of a single session. Idempotent: unknown or already revoked tokens are ignored. */
    public void revoke(String rawToken) {
        mongoTemplate.updateFirst(
                new Query(where("tokenHash").is(SecureTokens.hash(rawToken)).and("revoked").is(false)),
                new Update().set("revoked", true).set("revokedAt", Instant.now()), RefreshToken.class);
    }

    public void revokeAllForUser(String userId) {
        mongoTemplate.updateMulti(new Query(where("userId").is(userId).and("revoked").is(false)),
                new Update().set("revoked", true).set("revokedAt", Instant.now()), RefreshToken.class);
    }

    public void deleteAllForUser(String userId) {
        repository.deleteByUserId(userId);
    }

    public long getTtlSeconds() {
        return ttl.toSeconds();
    }
}
