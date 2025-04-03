package com.app.Order.service_order;

import org.springframework.stereotype.Service;

@Service
public class OrderMapper {
    public Order toOrder(OrderRequest orderRequest) {
        return Order.builder()
                .customerId(orderRequest.customerId())
                .reference(orderRequest.reference())
                .price(orderRequest.price())
                .paymentMethod(orderRequest.paymentMethod())
                .build();
    }

    public OrderResponse toOrderResponse(Order order) {
        return new OrderResponse(order.getId(),order.getReference(),order.getPrice(),order.getPaymentMethod(),order.getCustomerId());
    }
}
