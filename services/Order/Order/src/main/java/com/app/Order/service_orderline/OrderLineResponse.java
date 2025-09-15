package com.app.Order.service_orderline;

import com.app.Order.service_order.Order;

public record OrderLineResponse(Order order,
         Integer productId,
        double quantity) {
}
