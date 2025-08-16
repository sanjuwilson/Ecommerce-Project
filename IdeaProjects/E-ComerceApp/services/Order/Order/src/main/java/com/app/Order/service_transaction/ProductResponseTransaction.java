package com.app.Order.service_transaction;

import java.math.BigDecimal;

public record ProductResponseTransaction(
        Integer id,
        double quantity,
        String category,
        String name,
        BigDecimal price
) {
}
