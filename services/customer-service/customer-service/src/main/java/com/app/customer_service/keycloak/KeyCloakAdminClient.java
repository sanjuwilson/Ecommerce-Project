package com.app.customer_service.keycloak;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class KeyCloakAdminClient {
    private final WebClient webClient;

    @Value("${keycloak.auth-server-url}")
    private String keycloakUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin-client-id}")
    private String clientId;

    @Value("${keycloak.admin-client-secret}")
    private String clientSecret;

    public KeyCloakAdminClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }
    public Mono<String> getAdminAccessToken() {
        return webClient.post()
                .uri(keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "client_credentials")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(tokenResponse -> tokenResponse.get("access_token").asText());
    }

    public Mono<Void> createUserAndSendActionsEmail(String username, String email, String firstName, String lastName, Roles role,Integer customerId) {
        Map<@NonNull String,@NonNull Object> payLoad = null;
        if(role.equals(Roles.user)){
             payLoad= Map.of(
                    "username", username,
                    "firstName", firstName,
                    "lastName", lastName,
                    "email", email,
                    "enabled", true,
                    "emailVerified", false,
                    "attributes", Map.of("customerId", List.of(customerId.toString()))

            );


        }
        else{
            payLoad= Map.of(
                    "username", username,
                    "firstName", firstName,
                    "lastName", lastName,
                    "email", email,
                    "enabled", true,
                    "emailVerified", false
                    );

        }
        Map<@NonNull String, @NonNull Object> finalPayLoad = payLoad;
        return getAdminAccessToken().flatMap(token -> {


            // Step 1: Create user
            return webClient.post()
                    .uri(keycloakUrl + "/admin/realms/" + realm + "/users")
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(finalPayLoad)
                    .exchangeToMono(response -> {
                        if (response.statusCode().is2xxSuccessful()) {
                            String location = response.headers().header("Location").stream().findFirst().orElse(null);
                            if (location == null) {
                                return Mono.error(new RuntimeException("Location header missing, cannot get user id"));
                            }
                            String userId = location.substring(location.lastIndexOf("/") + 1);
                            // Step 2: Assign role
                            return assignRealmRoleToUser(token, userId, role)

                                    .then(sendUpdatePasswordEmail(token, userId));
                        } else {
                            return response.createException().flatMap(Mono::error);
                        }
                    });
        });
    }

    private Mono<Void> assignRealmRoleToUser(String token, String userId, Roles role) {
        String roleName = role.name();

        return getRealmRoleByName(token, roleName)
                .flatMap(roleInfo -> {
                    List<Map<String, Object>> roleRepresentation = List.of(Map.of(
                            "id", roleInfo.get("id").asText(),
                            "name", roleInfo.get("name").asText()
                    ));

                    return webClient.post()
                            .uri(keycloakUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/realm")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(roleRepresentation)
                            .retrieve()
                            .toBodilessEntity()
                            .then();
                });
    }

    private Mono<Void> sendUpdatePasswordEmail(String token, String userId) {
        var actions = List.of("UPDATE_PASSWORD", "VERIFY_EMAIL");
        return webClient.put()
                .uri(keycloakUrl + "/admin/realms/" + realm + "/users/" + userId + "/execute-actions-email")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(actions)
                .retrieve()
                .toBodilessEntity()
                .then();
    }

    private Mono<JsonNode> getRealmRoleByName(String token, String roleName) {
        return webClient.get()
                .uri(keycloakUrl + "/admin/realms/" + realm + "/roles/" + roleName)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(JsonNode.class);
    }


    public Mono<JsonNode> exchangeCodeForTokens(String code, String codeVerifier, String redirectUri) {
        return webClient.post()
                .uri(keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", clientId)
                        .with("code", code)
                        .with("redirect_uri", redirectUri)
                        .with("client_secret", clientSecret)
                        .with("code_verifier", codeVerifier))

                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        (ClientResponse response) -> response.bodyToMono(String.class)
                                .flatMap((String errorBody) -> {
                                    log.error("Token exchange failed: {}", errorBody);
                                    return Mono.error(new RuntimeException("Token exchange failed: " + errorBody));
                                })
                )
                .bodyToMono(JsonNode.class);

    }

    public Mono<Void> logout(String refreshToken) {
        return webClient.post()
                .uri(keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/logout")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("refresh_token", refreshToken))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class).flatMap(errorBody -> {
                            log.error("Logout failed: {}", errorBody);
                            return Mono.error(new RuntimeException("Logout failed: " + errorBody));
                        })
                )
                .toBodilessEntity()
                .then(); // Return Mono<Void>
    }


    public JsonNode refreshAccessToken(String decryptedRefreshToken) {
        return webClient.post()
                .uri(keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "refresh_token")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("refresh_token", decryptedRefreshToken))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(error -> {
                                    log.error("Refresh token request failed: {}", error);
                                    return Mono.error(new RuntimeException("Refresh token exchange failed"));
                                })
                )
                .bodyToMono(JsonNode.class)
                .block();
    }

}
