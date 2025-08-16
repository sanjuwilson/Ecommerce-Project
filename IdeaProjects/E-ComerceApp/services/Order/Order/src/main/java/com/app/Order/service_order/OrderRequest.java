package com.app.Order.service_order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public record OrderRequest(
        Integer customerId,
        String cartReference,
        List<PurchaseRequest> purchase
) {
}
