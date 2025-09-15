package com.app.cart.service_order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PurchaseRequest(
        @NotNull(message="Product is mandatory") Integer id,
        @Min(1) @Positive(message="Quantity is mandatory") double quantity
) {
}
