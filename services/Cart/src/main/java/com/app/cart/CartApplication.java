package com.app.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
@ComponentScan(basePackages = {
		"com.app.cart", // your current service package
		"com.ecom"              // the external JwtConvertor package
})
public class CartApplication {
	public static void main(String[] args) {
		SpringApplication.run(CartApplication.class, args);
	}

}
