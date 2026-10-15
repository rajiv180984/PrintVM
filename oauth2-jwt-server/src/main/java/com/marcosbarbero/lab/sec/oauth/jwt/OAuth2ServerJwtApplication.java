package com.marcosbarbero.lab.sec.oauth.jwt;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariDataSource;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class OAuth2ServerJwtApplication {

	@Bean(name="dataSource")
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource mainDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	public static void main(String... args) {
		// JAXB's bytecode-injection optimizer can't define classes on Java 9+ and logs a
		// SEVERE PrivilegedActionException from Injector.<clinit>; use plain reflection instead.
		// Must be set before any JAXB class loads.
		System.setProperty("com.sun.xml.bind.v2.bytecode.ClassTailor.noOptimize", "true");
		SpringApplication.run(OAuth2ServerJwtApplication.class, args);
	}

}
