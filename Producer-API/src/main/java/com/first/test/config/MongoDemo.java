package com.first.test.config;

import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoDemo {
	public static void main(String[] args) {
		String mongoUri = System.getenv("MONGO_URI");
		if (mongoUri == null || mongoUri.isEmpty()) {
			throw new IllegalStateException("Set the MONGO_URI environment variable");
		}

		try (MongoClient mongoClient = new MongoClient(new MongoClientURI(mongoUri))) {
			List<String> list = mongoClient.getDatabaseNames();
			System.out.println(" Total Databased " + list.toString());
		}
	}
}
