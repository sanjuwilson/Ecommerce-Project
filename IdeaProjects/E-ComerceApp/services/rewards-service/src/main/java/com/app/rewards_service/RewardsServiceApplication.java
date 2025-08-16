package com.app.rewards_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableFeignClients
@EnableJpaAuditing
@ComponentScan(basePackages = {
		"com.app.rewards_service", // your current service package
		"com.ecom"              // the external JwtConvertor package
})
public class RewardsServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(RewardsServiceApplication.class, args);
	}
}
