package com.app.customer_service.customer;

import com.app.customer_service.address.AddressRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CustomerDto(
         Integer id,
         @NotNull(message = "Cant be null")
         String firstname,
         @NotNull(message = "Cant be null")
         String lastname,
         @NotNull(message = "Cant be null")
         @Email(message="email required")
         String email,
         List<AddressRequest> addresses) {
}
