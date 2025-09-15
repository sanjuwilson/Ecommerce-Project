package com.app.customer_service.address;

public record AddressResponse(
        String street,
        String houseNumber,
        int zipCode
) {
}
