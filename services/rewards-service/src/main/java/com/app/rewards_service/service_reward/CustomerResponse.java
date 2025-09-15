package com.app.rewards_service.service_reward;

public record CustomerResponse(
        Integer id,
        String firstname,
        String lastname,
        String email,
        AddressResponse address
) {
}
