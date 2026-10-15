package com.first.auth.repository;

import com.first.auth.model.OneTimeToken;
import com.first.auth.model.TokenType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OneTimeTokenRepository extends MongoRepository<OneTimeToken, String> {

    void deleteByUserIdAndType(String userId, TokenType type);

    void deleteByUserId(String userId);
}
