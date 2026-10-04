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
		SpringApplication.run(OAuth2ServerJwtApplication.class, args);
	}

}
