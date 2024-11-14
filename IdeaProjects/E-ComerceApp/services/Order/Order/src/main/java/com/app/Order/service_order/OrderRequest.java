package com.app.Order.service_order;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(
        Integer orderId,
        String reference,
        BigDecimal price,
        PaymentMethod paymentMethod,
        Integer customerId,
        List<PurchaseRequest> purchase
) {
}
