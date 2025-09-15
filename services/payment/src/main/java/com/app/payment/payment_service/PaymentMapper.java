package com.app.payment.payment_service;

import org.springframework.stereotype.Service;

@Service
public class PaymentMapper {
    public Payment toPayment(PaymentRequest request) {
        return Payment.builder().id(request.id()).OrderId(request.orderId()).paymentMethod(request.method()).amount(request.amount()).build();
    }
}
