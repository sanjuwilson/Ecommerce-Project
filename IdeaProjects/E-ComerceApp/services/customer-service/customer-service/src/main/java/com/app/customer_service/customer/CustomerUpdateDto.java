package com.app.customer_service.customer;


import com.app.customer_service.address.AddressRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CustomerUpdateDto(
        String firstname,
        String lastname,
        List<AddressRequest> addresses) {
}
