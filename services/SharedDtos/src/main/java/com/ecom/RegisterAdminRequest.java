package com.ecom;

public record RegisterAdminRequest(
        String firstName,
        String lastName,
        String token
) {
}
