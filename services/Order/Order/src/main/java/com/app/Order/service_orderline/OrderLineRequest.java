package com.app.Order.service_orderline;

import java.math.BigDecimal;

public record OrderLineRequest(Integer id, Integer orderId, Integer productId, double quantity, String category,
                               BigDecimal price,String name) {
}
