package com.app.Order.service_order;

public record AddressResponse(
        String street,
        String houseNumber,
        int zipCode
) {
}
