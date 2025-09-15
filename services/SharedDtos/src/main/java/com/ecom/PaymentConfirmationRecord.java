package com.ecom;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record PaymentConfirmationRecord(
        String orderReference,
        BigDecimal orderAmount,
        Map<TransactionMethod,BigDecimal> paymentMethods,
        String firstName,
        String lastName,
        String email
) {
}
