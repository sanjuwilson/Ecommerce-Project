package com.applyfast.jobapp.search;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebFluxConfig {
    @Bean
    public WebClient webClient() {
        return WebClient.builder().baseUrl("https://serpapi.com").build();
    }
}
