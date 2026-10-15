package com.first.test.config;

import java.util.Collections;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * JPA / Hibernate setup.
 *
 * <ul>
 * <li>DataSource: created by Spring Boot (HikariCP) from {@code spring.datasource.*}.</li>
 * <li>Hibernate: every {@code spring.jpa.properties.*} key is handed to Hibernate unchanged.</li>
 * </ul>
 * All values come from {@code config/config-<profile>.properties}; nothing is hard-coded here.
 * The EntityManagerFactory is still declared explicitly because the pom pins Hibernate 5.1,
 * which Spring Boot 2.1's own Hibernate auto-configuration does not support.
 */
@Configuration
@PropertySource(value = "file:${configpath}-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
public class JpaConfig {

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, Environment env) {
		Map<String, String> hibernateProperties = Binder.get(env)
				.bind("spring.jpa.properties", Bindable.mapOf(String.class, String.class))
				.orElse(Collections.emptyMap());

		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(dataSource);
		emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		emf.setPackagesToScan("com.first.test.model");
		emf.setPersistenceUnitName("default");
		emf.setJpaPropertyMap(hibernateProperties);
		return emf;
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
}
