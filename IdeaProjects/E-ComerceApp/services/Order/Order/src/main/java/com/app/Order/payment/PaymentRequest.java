package com.app.Order.payment;

import com.app.Order.service_customer.CustomerResponse;
import com.app.Order.service_order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod method,
        Integer orderId,
        String orderReference,
        CustomerResponse  customer

) {
}

