package com.first.test;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
//@EnableEurekaClient
@EnableScheduling
@ComponentScan(basePackages = "com.first.test")
@EnableAsync
@EnableCaching
public class FirstProducerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirstProducerApiApplication.class, args);
	}

	@Bean
	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(2);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("JDAsync-");
		executor.initialize();
		return executor;
	}
	/*
	 * @Bean public MultipartConfigElement multipartConfigElement() { return new
	 * MultipartConfigElement(""); }
	 * 
	 * @Bean public MultipartResolver multipartResolver() {
	 * org.springframework.web.multipart.commons.CommonsMultipartResolver
	 * multipartResolver = new
	 * org.springframework.web.multipart.commons.CommonsMultipartResolver();
	 * multipartResolver.setMaxUploadSize(1000000); return multipartResolver; }
	 */
}
