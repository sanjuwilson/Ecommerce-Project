package com.app.customer_service.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CustomerDto(
         Integer id,
         @NotNull(message = "Cant be null")
         String firstname,
         @NotNull(message = "Cant be null")
         String lastname,
         @NotNull(message = "Cant be null")
         @Email(message="email required")
         String email,
         AddressDto address) {
}
