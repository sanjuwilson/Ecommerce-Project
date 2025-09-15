package com.app.product_service.services;

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
