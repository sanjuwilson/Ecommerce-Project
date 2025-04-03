package com.app.cart.service_cart;

import com.app.cart.service_order.PaymentMethod;

import java.math.BigDecimal;

public record CheckOutRequest(
        String reference,
        BigDecimal price,
        PaymentMethod paymentMethod
) {
}
