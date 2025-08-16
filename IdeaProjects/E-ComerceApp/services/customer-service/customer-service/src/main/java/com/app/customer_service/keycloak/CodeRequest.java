package com.app.customer_service.keycloak;

public record CodeRequest(
        String code,String codeVerifier,String redirectUri
) {
}
