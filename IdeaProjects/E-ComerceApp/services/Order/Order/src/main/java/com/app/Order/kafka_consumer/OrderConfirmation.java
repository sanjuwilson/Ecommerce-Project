package com.app.Order.kafka_consumer;

import com.app.Order.service_customer.CustomerResponse;
import com.app.Order.service_order.PaymentMethod;
import com.app.Order.service_product.ProductResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(String orderReference,
                                BigDecimal totalAmount, PaymentMethod paymentMethod,
                                CustomerResponse customerResponse, List<ProductResponse> products) {


}
