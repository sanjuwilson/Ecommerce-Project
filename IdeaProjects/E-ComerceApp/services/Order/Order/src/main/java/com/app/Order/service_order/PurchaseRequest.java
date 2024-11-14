package com.app.Order.service_order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record  PurchaseRequest(
        @NotNull(message="Product is mandatory") Integer id,
        @Positive(message="Quantity is mandatory") double quantity
) {
}
