package com.app.rewards_service.service_details;

import java.math.BigDecimal;

public record ProductResponseTransaction(Integer productId, double quantity, String category, String name, BigDecimal price) {
}
