package com.first.test.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.mongodb.ServerAddress;

@PropertySource(value = "file:${configpath}-${spring.profiles.active}.properties", ignoreResourceNotFound = true)@ConfigurationProperties
@Configuration("mongoDBConfigeration")
public class MongoDBConfigeration implements Serializable {
	private static final long serialVersionUID = -5904396605022945919L;

	@Value("${mongo.serverAddresses}")
	private String serverAddresses;

	@Value("${mongo.userName}")
	private String userNameValue;

	@Value("${mongo.password}")
	private String password;

	@Value("${mongo.databaseName}")
	private String databaseName;

	private List<ServerAddress> serverList;

	@Value("${mongo.connectionsPerHost}")
	private int connectionsPerHost;

	@Value("${mongo.connectionTimeout}")
	private int connectionTimeout;

	@Value("${mongo.maxWaitTime}")
	private long maxWaitTime;

	@Value("${mongo.threadsAllowedToBlock}")
	private int threadsAllowedToBlock;

	@Value("${mongo.socketKeepAlive}")
	private long socketKeepAlive;

	@Value("${mongo.heartbeatConnectTimeout}")
	private int heartbeatConnectTimeout;

	@Value("${mongo.minHeartbeatFrequency}")
	private long minHeartbeatFrequency;

	@Value("${mongo.maxConnectionIdleTime}")
	private int maxConnectionIdleTime;

	@Value("${mongo.retryWrites}")
	private boolean retryWrites;

	@Value("${mongo.sslEnabled}")
	private boolean sslEnabled;
	
	@Value("${mongo.socketTimeout}")
	private int socketTimeout;

	public String getServerAddresses() {
		return serverAddresses;
	}

	public void setServerAddresses(String serverAddresses) {
		this.serverAddresses = serverAddresses;
	}

	
	public String getUserNameValue() {
		return userNameValue;
	}

	public void setUserNameValue(String userNameValue) {
		this.userNameValue = userNameValue;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public List<ServerAddress> getServerList() {
		return this.serverList;
	}

	public void setServerList() {
		List<ServerAddress> serverList = new ArrayList();
		String servlerInfo[] = serverAddresses.split(",");
		for(String server:servlerInfo ) {
			String oneServer[] = server.split(":");
			this.serverList.add( new ServerAddress(oneServer[0], Integer.parseInt(oneServer[1])));
					
		}
		this.serverList = serverList;
	}

	public int getConnectionsPerHost() {
		return connectionsPerHost;
	}

	public void setConnectionsPerHost(int connectionsPerHost) {
		this.connectionsPerHost = connectionsPerHost;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public long getMaxWaitTime() {
		return maxWaitTime;
	}

	public void setMaxWaitTime(long maxWaitTime) {
		this.maxWaitTime = maxWaitTime;
	}

	public int getThreadsAllowedToBlock() {
		return threadsAllowedToBlock;
	}

	public void setThreadsAllowedToBlock(int threadsAllowedToBlock) {
		this.threadsAllowedToBlock = threadsAllowedToBlock;
	}

	public long getSocketKeepAlive() {
		return socketKeepAlive;
	}

	public void setSocketKeepAlive(long socketKeepAlive) {
		this.socketKeepAlive = socketKeepAlive;
	}

	public int getHeartbeatConnectTimeout() {
		return heartbeatConnectTimeout;
	}

	public void setHeartbeatConnectTimeout(int heartbeatConnectTimeout) {
		this.heartbeatConnectTimeout = heartbeatConnectTimeout;
	}

	public long getMinHeartbeatFrequency() {
		return minHeartbeatFrequency;
	}

	public void setMinHeartbeatFrequency(long minHeartbeatFrequency) {
		this.minHeartbeatFrequency = minHeartbeatFrequency;
	}

	public int getMaxConnectionIdleTime() {
		return maxConnectionIdleTime;
	}

	public void setMaxConnectionIdleTime(int maxConnectionIdleTime) {
		this.maxConnectionIdleTime = maxConnectionIdleTime;
	}

	public boolean isRetryWrites() {
		return retryWrites;
	}

	public void setRetryWrites(boolean retryWrites) {
		this.retryWrites = retryWrites;
	}

	public boolean isSslEnabled() {
		return sslEnabled;
	}

	public void setSslEnabled(boolean sslEnabled) {
		this.sslEnabled = sslEnabled;
	}

	public void setServerList(List<ServerAddress> serverList) {
		serverList = (List<ServerAddress>) new ArrayList();
		String servlerInfo[] = serverAddresses.split(",");
		for(String server:servlerInfo ) {
			String oneServer[] = server.split(":");
			this.serverList.add( new ServerAddress(oneServer[0], Integer.parseInt(oneServer[1])));
					
		}
		this.serverList = serverList;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	@Override
	public String toString() {
		return "MongoDBConfigeration [serverAddresses=" + serverAddresses + ", userNameValue=" + userNameValue
				+ ", password=" + password + ", databaseName=" + databaseName + ", serverList=" + serverList
				+ ", connectionsPerHost=" + connectionsPerHost + ", connectionTimeout=" + connectionTimeout
				+ ", maxWaitTime=" + maxWaitTime + ", threadsAllowedToBlock=" + threadsAllowedToBlock
				+ ", socketKeepAlive=" + socketKeepAlive + ", heartbeatConnectTimeout=" + heartbeatConnectTimeout
				+ ", minHeartbeatFrequency=" + minHeartbeatFrequency + ", maxConnectionIdleTime="
				+ maxConnectionIdleTime + ", retryWrites=" + retryWrites + ", sslEnabled=" + sslEnabled
				+ ", socketTimeout=" + socketTimeout + "]";
	}

	

}
