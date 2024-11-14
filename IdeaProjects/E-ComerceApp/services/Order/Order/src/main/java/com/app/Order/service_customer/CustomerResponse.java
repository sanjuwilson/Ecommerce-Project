package com.app.Order.service_customer;

import com.app.Order.service_order.AddressResponse;

public record CustomerResponse(
        Integer id,
        String firstname,
        String lastname,
        String email,
        AddressResponse address) {
}
