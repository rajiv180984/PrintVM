package com.first.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Enables {@code @CreatedDate} / {@code @LastModifiedDate} on documents. The connection itself is
 * configured through {@code spring.data.mongodb.uri} (env var {@code MONGODB_URI}).
 */
@Configuration
@EnableMongoAuditing
public class MongoConfig {
}
