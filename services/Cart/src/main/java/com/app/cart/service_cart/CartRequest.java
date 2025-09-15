package com.app.cart.service_cart;

import com.app.cart.service_details.ProductDetails;
import com.app.cart.service_details.ProductDetailsRequest;
import com.ecom.CartStatus;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CartRequest(CartStatus status, List<ProductDetailsRequest> detailsRequests) {
}