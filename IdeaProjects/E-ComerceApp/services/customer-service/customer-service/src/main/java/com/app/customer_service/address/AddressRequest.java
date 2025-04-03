package com.app.customer_service.address;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddressRequest(
        @NotNull(message = "Cant be null")
        String street,
        @NotNull(message = "Cant be null")
        String houseNumber,
        @Size(min=5,message = "Should be more or equal to 5 digits")
        int zipCode,
        int customerId
) {
}
