package com.ecom;

public record ProductPointsRecord(
        String productName,
        double quantity,
        Integer points,
        double cashPerPoint
) {
}
