package com.app.customer_service.customer;

public record AddressResponse(
        String street,
        String houseNumber,
        int zipCode
) {
}
