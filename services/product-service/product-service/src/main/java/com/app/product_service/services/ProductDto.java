package com.app.product_service.services;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductDto(
        Integer id,
        @NotNull(message = "Cannot Be null")
        String name,
        @Min(1)
        double availableQuantity,
        @DecimalMin("0.1")
        BigDecimal price,
        @NotNull(message = "Cannot be null")
        Integer category_id
) {
}
