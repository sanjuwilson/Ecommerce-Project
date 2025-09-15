package com.app.Order.service_order;

import org.springframework.stereotype.Service;

@Service
public class OrderMapper {
    public OrderResponse toOrderResponse(Order order) {
        return new OrderResponse(order.getId(),order.getReference(),order.getPrice(),order.getCustomerId());
    }
}
