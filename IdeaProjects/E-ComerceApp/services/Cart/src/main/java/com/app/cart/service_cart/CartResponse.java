package com.app.cart.service_cart;

import com.app.cart.service_details.ProductDetailsRequest;
import com.app.cart.service_details.ProductDetailsResponse;

import java.util.List;

public record CartResponse(int userId, CartStatus status, List<ProductDetailsResponse> responses) {
}
