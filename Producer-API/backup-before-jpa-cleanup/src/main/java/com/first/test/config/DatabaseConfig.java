package com.first.test.config;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = "file:${configpath}-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
@ConfigurationProperties(prefix = "spring.datasource")
@Configuration("databaseConfig")
public class DatabaseConfig implements Serializable{

	private static final long serialVersionUID = -6691432280603741485L;
	
	@Value("${spring.datasource.rdbmsvendorName}")
	private String rdbmsvendorName;
	
	@Value("${spring.datasource.username}")
	private String userName;
	
	@Value("${spring.datasource.password}")
	private String password;
	
	@Value("${spring.datasource.url}")
	private String url;
	
	@Value("${spring.datasource.dataSourceClassName}")
	private String dataSourceClassName;
	
	
	@Value("${spring.datasource.maximumPoolSize}")
	private int maximumPoolSize;
	
	@Value("${spring.datasource.testWhileIdle}")
	private String testWhileIdle;
	
	@Value("${spring.datasource.cachePrepStmts}")
	private String cachePrepStmts;
	
	@Value("${spring.datasource.prepStmtCacheSize}")
	private String prepStmtCacheSize;
	
	@Value("${spring.datasource.prepStmtCacheSqlLimit}")
	private String prepStmtCacheSqlLimit;
	
	@Value("${spring.datasource.useServerPrepStmts}")
	private String useServerPrepStmts;

	@Value("${spring.datasource.serverName}")
	private String serverName;
	
	@Value("${spring.datasource.portNumber}")
	private String portNumber;
	
	@Value("${spring.datasource.databaseName}")
	private String databaseName;
	
	
	
	public String getRdbmsvendorName() {
		return rdbmsvendorName;
	}

	public void setRdbmsvendorName(String rdbmsvendorName) {
		this.rdbmsvendorName = rdbmsvendorName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDataSourceClassName() {
		return dataSourceClassName;
	}

	public int getMaximumPoolSize() {
		return maximumPoolSize;
	}

	public void setMaximumPoolSize(int maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
	}

	public void setDataSourceClassName(String dataSourceClassName) {
		this.dataSourceClassName = dataSourceClassName;
	}

	public String getTestWhileIdle() {
		return testWhileIdle;
	}

	public void setTestWhileIdle(String testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}

	
	public String getCachePrepStmts() {
		return cachePrepStmts;
	}

	public void setCachePrepStmts(String cachePrepStmts) {
		this.cachePrepStmts = cachePrepStmts;
	}

	public String getPrepStmtCacheSize() {
		return prepStmtCacheSize;
	}

	public void setPrepStmtCacheSize(String prepStmtCacheSize) {
		this.prepStmtCacheSize = prepStmtCacheSize;
	}

	public String getPrepStmtCacheSqlLimit() {
		return prepStmtCacheSqlLimit;
	}

	public void setPrepStmtCacheSqlLimit(String prepStmtCacheSqlLimit) {
		this.prepStmtCacheSqlLimit = prepStmtCacheSqlLimit;
	}

	public String getUseServerPrepStmts() {
		return useServerPrepStmts;
	}

	public void setUseServerPrepStmts(String useServerPrepStmts) {
		this.useServerPrepStmts = useServerPrepStmts;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	@Override
	public String toString() {
		return "DatabaseConfig [userName=" + userName + ", password=" + password + ", url=" + url
				+ ", dataSourceClassName=" + dataSourceClassName + ", maximumPoolSize=" + maximumPoolSize
				+ ", testWhileIdle=" + testWhileIdle + ", cachePrepStmts=" + cachePrepStmts + ", prepStmtCacheSize="
				+ prepStmtCacheSize + ", prepStmtCacheSqlLimit=" + prepStmtCacheSqlLimit + ", useServerPrepStmts="
				+ useServerPrepStmts + ", serverName=" + serverName + ", portNumber=" + portNumber + ", databaseName="
				+ databaseName + "]";
	}

	

	
	
	
}
