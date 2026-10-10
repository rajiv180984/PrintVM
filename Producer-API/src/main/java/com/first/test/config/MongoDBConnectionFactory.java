package com.first.test.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@Configuration
@EnableMongoAuditing
@ComponentScan({ "com.jio.motive" })
@PropertySource(value = "file:${configpath}-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
public class MongoDBConnectionFactory {

	// Full connection string incl. credentials, e.g. mongodb+srv://user:pass@host/admin?retryWrites=true&w=majority
	@Value("${mongo.uri}")
	private String mongoUri;

	@Value("${mongo.appDatabaseName}")
	private String appDatabaseName;

	@Bean
	public MongoClient mongoClient() {
		return new MongoClient(new MongoClientURI(mongoUri));
	}

	@Bean
	public MongoTemplate mongoTemplate(MongoClient mongoClient) {
		return new MongoTemplate(mongoClient, appDatabaseName);
	}
}
