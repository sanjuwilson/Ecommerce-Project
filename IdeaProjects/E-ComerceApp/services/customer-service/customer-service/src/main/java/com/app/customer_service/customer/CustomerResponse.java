package com.app.customer_service.customer;

public record CustomerResponse(
         Integer id,
         String firstname,
         String lastname,
         String email,
         AddressResponse address) {
}
