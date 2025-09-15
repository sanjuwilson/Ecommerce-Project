package com.app.rewards_service.service_reward;

import com.app.rewards_service.service_details.ProductResponseTransaction;

import java.math.BigDecimal;
import java.util.List;

public record TransactionResponse(CustomerResponse customerResponse,BigDecimal amount, Integer customerId, List<ProductResponseTransaction> request,String cartReference) {
}
