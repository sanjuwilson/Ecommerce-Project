package com.applyfast.jobapp.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SearchWithAPI {
    private final WebClient webClient;

    private static final String API_KEY = "ae87c0d7db7a13b865ed438cd5a734a1548721373cc70977b4beae94b56491bc";

    public Mono<JsonNode> search(String query) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search.json")
                        .queryParam("engine", "google")
                        .queryParam("q", query)
                        .queryParam("api_key", API_KEY)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .map(this::parseJson);
    }

    private JsonNode parseJson(String json) {
        try {
            return new ObjectMapper().readTree(json);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response", e);
        }
    }
}
