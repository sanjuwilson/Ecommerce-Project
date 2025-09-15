package com.app.cart.service_details;

public record ProductDetailsRequest(
        int productId,
        double quantity
) {
}
