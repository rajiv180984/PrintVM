package com.first.test.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan({ "com.first.test" })
public class HibernateConnectionDriver {
	private Logger logger = LoggerFactory.getLogger(HibernateConnectionDriver.class);
	@Autowired
	HibernateConfigeration hibernateProperties;

	@Autowired
	DatabaseConfig databaseConfig;

	@Autowired
	@Qualifier(value="entityManagerFactory")
	private EntityManagerFactory entityManagerFactory;

	@Bean
	public DataSource dataSource() {

		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setMaximumPoolSize(databaseConfig.getMaximumPoolSize());
		dataSource.setDataSourceClassName(databaseConfig.getDataSourceClassName());
		//
		dataSource.addDataSourceProperty("user", databaseConfig.getUserName());
		dataSource.addDataSourceProperty("password", databaseConfig.getPassword());

		//if (databaseConfig.getRdbmsvendorName().equalsIgnoreCase("mysql")) {
			dataSource.addDataSourceProperty("url", databaseConfig.getUrl());
			dataSource.addDataSourceProperty("autoReconnect", false);
			//dataSource.addDataSourceProperty("prepStmtCacheSize", databaseConfig.getPrepStmtCacheSize());
			dataSource.addDataSourceProperty("useServerPrepStmts", databaseConfig.getUseServerPrepStmts());
		//	dataSource.addDataSourceProperty("cachePrepStmts", databaseConfig.getCachePrepStmts());
			dataSource.addDataSourceProperty("useSSL", false);
			dataSource.addDataSourceProperty("autoReconnect", true);
			dataSource.addDataSourceProperty("databaseName","zeroxUser");
		/*} else if (databaseConfig.getRdbmsvendorName().equalsIgnoreCase("postgres")) {
			// dataSource.addDataSourceProperty("serverName", "13.233.174.73");
			dataSource.addDataSourceProperty("serverName", databaseConfig.getServerName());
			dataSource.addDataSourceProperty("portNumber", databaseConfig.getPortNumber());
			dataSource.addDataSourceProperty("databaseName","zeroxUser");
		}
*/
		dataSource.setPoolName("mysql_HIKARI_VENDOR");
		dataSource.setConnectionTimeout(100000l);

		return dataSource;
	}

	@Bean
	public SessionFactory getSessionFactory() {
		if (entityManagerFactory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}
		return entityManagerFactory.unwrap(SessionFactory.class);
	}

	@Bean(name = "entityManagerFactory")
	public EntityManagerFactory entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean emf = null;
		try {
			emf = new LocalContainerEntityManagerFactoryBean();
			DataSource dataSource = dataSource();

			if (dataSource == null) {
				logger.error(" Failed to create Datasource ");
				throw new DataSourceLookupFailureException("Failed to create Datasource");
			}
			JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
			emf.setJpaProperties(getHibernateProperties());
			emf.setBeanName("hibernateProperties");
			emf.setDataSource(dataSource);
			emf.setJpaVendorAdapter(jpaVendorAdapter);
			emf.setPackagesToScan("com.first.test");
			emf.setPersistenceUnitName("default");

			emf.afterPropertiesSet();

		} catch (Exception e) {
			logger.error("!!!! Enity Manager instantiation failed ", e);
		}
		return emf.getObject();
	}

	@Bean
	public Properties getHibernateProperties() {
		logger.info("============== Hibernate propertie "+ hibernateProperties.toString());
		Properties ormProperties = new Properties();
		ormProperties.put(Environment.DIALECT, hibernateProperties.getDialect());
		ormProperties.put(Environment.SHOW_SQL, hibernateProperties.getShowSql());
		ormProperties.put(Environment.HBM2DDL_AUTO, hibernateProperties.getDdlAuto());
		ormProperties.put("hibernate.naming-strategy", hibernateProperties.getNamingStrategy());
		ormProperties.put(Environment.SHOW_SQL, hibernateProperties.getShowSql());
		ormProperties.put(Environment.FORMAT_SQL, true);
		ormProperties.put(Environment.USE_SQL_COMMENTS, true);
		ormProperties.put(Environment.NON_CONTEXTUAL_LOB_CREATION, true);
		
		//  ormProperties.put("hibernate.cache.use_query_cache",hibernateProperties.
		  //getUseQueryCache());
		  /*ormProperties.put("hibernate.cache.use_second_level_cache",
		  hibernateProperties.getUseSecondLevelCache());
		  ormProperties.put("hibernate.cache.default_cache_concurrency_strategy",
		  hibernateProperties.getCacheConcurrencyStrategy());
		  ormProperties.put("hibernate.cache.region.factory_class",hibernateProperties.
		  getFactoryClass());
		 */
		ormProperties.put("hibernate.enable_lazy_load_no_trans", hibernateProperties.getEnableLazyLoadNoTrans());
		return ormProperties;
	}

}
