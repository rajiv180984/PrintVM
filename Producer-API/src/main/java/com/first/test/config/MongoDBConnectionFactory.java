package com.first.test.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoDatabase;

@Configuration
@EnableMongoAuditing
@ComponentScan({ "com.jio.motive" })
public class MongoDBConnectionFactory {
	@Autowired
	MongoDBConfigeration mongoDBConfigeration;
	private MongoTemplate mongoTemplate;

	private MongoClient mongoEclient;

	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	@Bean
	public MongoClient getMongoDBClient() {

/*		
		ServerAddress serverAddress = new ServerAddress();
		List<ServerAddress> seeds = new ArrayList<>();
		
		String servlerInfo[] = mongoDBConfigeration.getServerAddresses().split(",");
		for(String server:servlerInfo ) {
			String oneServer[] = server.split(":");
			seeds.add( new ServerAddress(oneServer[0], Integer.parseInt(oneServer[1])));
					
		}
		//seeds.add(new ServerAddress("127.0.0.1", 27017));
		MongoCredential credential = MongoCredential.createCredential(mongoDBConfigeration.getUserNameValue(),
				mongoDBConfigeration.getDatabaseName(), mongoDBConfigeration.getPassword().toCharArray());

		List<MongoCredential> credentials = new ArrayList<MongoCredential>();
		credentials.add(
		    MongoCredential.createMongoCRCredential(
		        mongoDBConfigeration.getUserNameValue(),
		        mongoDBConfigeration.getDatabaseName(),
		        mongoDBConfigeration.getPassword().toCharArray()
		    )
		);
		
		MongoClientOptions options = MongoClientOptions.builder()
				.threadsAllowedToBlockForConnectionMultiplier(mongoDBConfigeration.getThreadsAllowedToBlock())
				.connectionsPerHost(mongoDBConfigeration.getConnectionsPerHost())
				.connectTimeout(mongoDBConfigeration.getConnectionTimeout())
				.maxWaitTime(mongoDBConfigeration.getConnectionTimeout())
				.socketTimeout(mongoDBConfigeration.getSocketTimeout())
				.heartbeatConnectTimeout(mongoDBConfigeration.getHeartbeatConnectTimeout())
				.sslEnabled(false)
				.writeConcern(WriteConcern.ACKNOWLEDGED).build();

		
		mongoEclient = new MongoClient(seeds,credential, options); //new MongoClient(seeds, credential, options);
*/
MongoClientURI uri = new MongoClientURI(
    "mongodb://zeroxvm:2rGpQdguDydtleOA@zeroxvm-shard-00-00-n6ojx.mongodb.net:27017,zeroxvm-shard-00-01-n6ojx.mongodb.net:27017,zeroxvm-shard-00-02-n6ojx.mongodb.net:27017/admin?ssl=true&replicaSet=Zeroxvm-shard-0&authSource=admin&retryWrites=true&w=majority");

MongoClient mongoClient = new MongoClient(uri);
MongoDatabase database = mongoClient.getDatabase("zeroxvmDB");

		return mongoClient;

	}

	/*@Bean
	public MongoDbFactory mongoDbFactory() throws Exception {
		//UserCredentials userCredentials = new UserCredentials("joe", "secret");
		return new SimpleMongoDbFactory(getMongoDBClient(), "zeroxvmDB");
	}
*/
	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		MongoClientURI uri = new MongoClientURI(
			    "mongodb://zeroxvm:sedXtVxaK24RGJxF@zeroxvm-shard-00-00-n6ojx.mongodb.net:27017,zeroxvm-shard-00-01-n6ojx.mongodb.net:27017,zeroxvm-shard-00-02-n6ojx.mongodb.net:27017/admin?ssl=true&replicaSet=Zeroxvm-shard-0&authSource=admin&retryWrites=true&w=majority");

			MongoClient mongoClient = new MongoClient(uri);
			
			List<String> list = mongoClient.getDatabaseNames();
			System.out.println(" Total Databased " + list.toString());
		return new MongoTemplate(mongoClient, "zeroxvmDB");
	}
}