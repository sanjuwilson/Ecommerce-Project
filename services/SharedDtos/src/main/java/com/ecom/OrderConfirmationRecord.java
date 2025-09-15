package com.ecom;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmationRecord(
        String orderReference,
        BigDecimal totalAmount,
        CustomerRecord customer,
        List<ProductRecord> products,
        List<TransactionMethod>paymentMethods
) {
}
