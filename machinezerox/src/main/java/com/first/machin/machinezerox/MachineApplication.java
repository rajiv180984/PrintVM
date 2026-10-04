package com.first.machin.machinezerox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAutoConfiguration
@EnableScheduling
@ComponentScan(basePackages= {"com.first.test.controller","com.first.machin","com.first.hivemqtt"})
public class MachineApplication {

	public static void main(String[] args) {
		SpringApplication.run(MachineApplication.class, args);
	}

	
}
