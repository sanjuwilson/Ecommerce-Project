package com.app.payment.payment_service;

import java.math.BigDecimal;

public record PaymentRequest(
        Integer id,
        BigDecimal amount,
        Integer orderId,
        String orderReference,
        Customer customer,
        PaymentMethod method
) {
}
