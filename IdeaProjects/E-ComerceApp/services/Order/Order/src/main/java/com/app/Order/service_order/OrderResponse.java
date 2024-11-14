package com.app.Order.service_order;

import java.math.BigDecimal;

public record OrderResponse(Integer id, String reference, BigDecimal amount,PaymentMethod paymentMethod,Integer customerId) {
}
