package com.app.rewards_service.service_reward;

public record AddressResponse(
        String street,
        String houseNumber,
        int zipCode
) {
}
