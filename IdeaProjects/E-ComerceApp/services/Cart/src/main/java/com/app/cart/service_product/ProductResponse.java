package com.app.cart.service_product;

import java.math.BigDecimal;

public record ProductResponse(
        Integer id,
        String name,
        double availableQuantity,
        BigDecimal price,
        Integer categoryId,
        String description
) {
}