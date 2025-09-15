package com.app.Order.service_transaction;

import com.app.Order.service_customer.CustomerResponse;
import com.app.Order.service_order.PurchaseRequest;

import java.math.BigDecimal;
import java.util.List;

public record TransactionResponse(
        CustomerResponse customerResponse,
        BigDecimal amount, Integer customerId, List<ProductResponseTransaction>request,String cartReference
        ){
}
