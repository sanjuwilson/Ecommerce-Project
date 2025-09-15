package com.ecom;

import java.math.BigDecimal;

public record ProductRecord(
        String name,
        BigDecimal price,
        String description,
        double quantity
) {
}
