package com.app.Order.service_orderline;

public record OrderLineRequest(Integer id,Integer orderId,Integer productId,double quantity) {
}
