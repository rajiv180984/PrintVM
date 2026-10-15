package com.first.auth.service;

import com.first.auth.exception.ApiException;
import com.first.auth.model.OneTimeToken;
import com.first.auth.model.TokenType;
import com.first.auth.repository.OneTimeTokenRepository;
import com.first.auth.security.SecureTokens;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Single-use tokens for password reset and email verification.
 */
@Service
public class OneTimeTokenService {

    private final OneTimeTokenRepository repository;
    private final MongoTemplate mongoTemplate;

    public OneTimeTokenService(OneTimeTokenRepository repository, MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    /** Issues a new token, invalidating any earlier token of the same type for the user. */
    public String create(String userId, TokenType type, Duration ttl) {
        repository.deleteByUserIdAndType(userId, type);
        String raw = SecureTokens.generate();
        Instant now = Instant.now();
        OneTimeToken token = new OneTimeToken();
        token.setTokenHash(SecureTokens.hash(raw));
        token.setUserId(userId);
        token.setType(type);
        token.setCreatedAt(now);
        token.setExpiresAt(now.plus(ttl));
        repository.save(token);
        return raw;
    }

    /**
     * Atomically removes the token so it can be used once only.
     *
     * @return the id of the user the token belongs to
     * @throws ApiException 400 if the token is unknown, already used or expired
     */
    public String consume(String rawToken, TokenType type) {
        Query query = new Query(where("tokenHash").is(SecureTokens.hash(rawToken)).and("type").is(type));
        OneTimeToken token = mongoTemplate.findAndRemove(query, OneTimeToken.class);
        if (token == null || token.isExpired()) {
            throw ApiException.badRequest("Invalid or expired token");
        }
        return token.getUserId();
    }

    public void deleteAllForUser(String userId) {
        repository.deleteByUserId(userId);
    }
}
