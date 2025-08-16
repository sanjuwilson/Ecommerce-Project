package com.ecom;

import java.time.LocalDateTime;

public record OrderUpdater(
        String reference, LocalDateTime now,OrderStatus orderStatus
) {
}
