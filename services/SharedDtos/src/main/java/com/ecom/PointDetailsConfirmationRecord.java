package com.ecom;

import java.math.BigDecimal;
import java.util.List;

public record PointDetailsConfirmationRecord(
        int totalPoints,
        int pointsEarned,
        double equivalentCashEarned,
        String customerName,
        String email,
        String orderReference,
        List<ProductPointsRecord> productPointsRecordList
) {
}
