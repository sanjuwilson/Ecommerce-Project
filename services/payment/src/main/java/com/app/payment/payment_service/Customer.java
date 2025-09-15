package com.app.payment.payment_service;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record Customer(
        Integer Id,
        @NotNull
        String firstName,
        @NotNull
        String lastName,
        @Email
        String email

) {
}
