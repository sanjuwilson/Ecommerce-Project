package com.app.Order.service_orderline;

import com.app.Order.service_order.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderLineMapper {
    public OrderLine toOrderLine(OrderLineRequest request) {
        return OrderLine.builder()
                .productId(request.productId())
                .order(Order.builder().
                        id(request.orderId()).
                        build())
                .Id(request.id())
                .quantity(request.quantity())
                .build();
    }
    public OrderLineResponse toOrderLineResponse(OrderLine line) {
        return new OrderLineResponse(line.getOrder(),line.getProductId(),line.getQuantity());
    }
}
