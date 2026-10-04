package com.first.test.config;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoDemo {
	public static void main(String[] args) {
		MongoClientURI uri = new MongoClientURI(
				"mongodb://zeroxvm:sedXtVxaK24RGJxF@zeroxvm-shard-00-00-n6ojx.mongodb.net:27017,zeroxvm-shard-00-01-n6ojx.mongodb.net:27017,zeroxvm-shard-00-02-n6ojx.mongodb.net:27017/admin?ssl=true&replicaSet=Zeroxvm-shard-0&authSource=admin&retryWrites=true&w=majority");

		MongoClient mongoClient = new MongoClient(uri);

		MongoTemplate mtemp = new MongoTemplate(mongoClient, "zeroxvmDB");

		List<String> list = mongoClient.getDatabaseNames();
		System.out.println(" Total Databased " + list.toString());
		
		

	}
}
