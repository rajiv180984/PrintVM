package com.first.test.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "spring.jpa.hibernate")
@Configuration("hibernateProperties")
public class HibernateConfigeration{

	private static final long serialVersionUID = -5904396605022945919L;

	@Autowired
	DatabaseConfig databaseConfig;

	@Value("${spring.jpa.hibernate.dialect}")
	private String dialect;

	@Value("${spring.jpa.hibernate.show-sql}")
	private String showSql;

	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String ddlAuto;

	@Value("${spring.jpa.hibernate.naming-strategy}")
	private String namingStrategy;

	@Value("${spring.jpa.hibernate.cache.use_query_cache}")
	private String useQueryCache;

	@Value("${spring.jpa.hibernate.cache.use_second_level_cache}")
	private String useSecondLevelCache;

	@Value("${spring.jpa.hibernate.cache.default_cache_concurrency_strategy}")
	private String cacheConcurrencyStrategy;

	@Value("${spring.jpa.hibernate.cache.region.factory_class}")
	private String factoryClass;

	@Value("${spring.jpa.hibernate.enable_lazy_load_no_trans}")
	private String enableLazyLoadNoTrans;

	public String getDialect() {
		return dialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	public String getShowSql() {
		return showSql;
	}

	public void setShowSql(String showSql) {
		this.showSql = showSql;
	}

	public String getDdlAuto() {
		return ddlAuto;
	}

	public void setDdlAuto(String ddlAuto) {
		this.ddlAuto = ddlAuto;
	}

	public String getNamingStrategy() {
		return namingStrategy;
	}

	public void setNamingStrategy(String namingStrategy) {
		this.namingStrategy = namingStrategy;
	}

	public String getUseQueryCache() {
		return useQueryCache;
	}

	public void setUseQueryCache(String useQueryCache) {
		this.useQueryCache = useQueryCache;
	}

	public String getUseSecondLevelCache() {
		return useSecondLevelCache;
	}

	public void setUseSecondLevelCache(String useSecondLevelCache) {
		this.useSecondLevelCache = useSecondLevelCache;
	}

	public String getCacheConcurrencyStrategy() {
		return cacheConcurrencyStrategy;
	}

	public void setCacheConcurrencyStrategy(String cacheConcurrencyStrategy) {
		this.cacheConcurrencyStrategy = cacheConcurrencyStrategy;
	}

	public String getFactoryClass() {
		return factoryClass;
	}

	public void setFactoryClass(String factoryClass) {
		this.factoryClass = factoryClass;
	}

	public String getEnableLazyLoadNoTrans() {
		return enableLazyLoadNoTrans;
	}

	public void setEnableLazyLoadNoTrans(String enableLazyLoadNoTrans) {
		this.enableLazyLoadNoTrans = enableLazyLoadNoTrans;
	}

	
	public DatabaseConfig getDatabaseConfig() {
		return databaseConfig;
	}

	public void setDatabaseConfig(DatabaseConfig databaseConfig) {
		this.databaseConfig = databaseConfig;
	}

	@Override
	public String toString() {
		return "HibernateConfigeration [databaseConfig=" + databaseConfig + ", dialect=" + dialect + ", showSql="
				+ showSql + ", ddlAuto=" + ddlAuto + ", namingStrategy=" + namingStrategy + ", useQueryCache="
				+ useQueryCache + ", useSecondLevelCache=" + useSecondLevelCache + ", cacheConcurrencyStrategy="
				+ cacheConcurrencyStrategy + ", factoryClass=" + factoryClass + ", enableLazyLoadNoTrans="
				+ enableLazyLoadNoTrans + "]";
	}

	
}
