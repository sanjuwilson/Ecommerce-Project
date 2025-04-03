package com.app.customer_service.customer;

import com.app.customer_service.address.AddressResponse;

import java.util.List;

public record CustomerResponse(
         Integer id,
         String firstname,
         String lastname,
         String email,
         List<AddressResponse> addresses) {
}
