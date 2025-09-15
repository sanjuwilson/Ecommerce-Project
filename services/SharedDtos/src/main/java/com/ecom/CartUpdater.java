package com.ecom;

import java.time.LocalDateTime;

public record CartUpdater(
        String cartReference, LocalDateTime now,CartStatus cartStatus
        ) {
}
