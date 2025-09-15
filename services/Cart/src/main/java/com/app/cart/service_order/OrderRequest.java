package com.app.cart.service_order;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(
        String cartReference,
        Integer customerId,
        List<PurchaseRequest> purchase
) {
}
