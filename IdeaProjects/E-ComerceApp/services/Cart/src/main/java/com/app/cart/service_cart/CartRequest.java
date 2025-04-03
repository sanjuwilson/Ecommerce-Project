package com.app.cart.service_cart;

import com.app.cart.service_details.ProductDetails;
import com.app.cart.service_details.ProductDetailsRequest;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CartRequest(@NotNull int userId, CartStatus status, List<ProductDetailsRequest> detailsRequests) {
}