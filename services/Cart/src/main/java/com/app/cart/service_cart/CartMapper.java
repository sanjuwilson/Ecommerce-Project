package com.app.cart.service_cart;

import com.app.cart.service_details.ProductDetails;
import com.app.cart.service_details.ProductDetailsMapper;
import com.app.cart.service_details.ProductDetailsResponse;
import com.app.cart.service_order.PurchaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartMapper {
    private final ProductDetailsMapper productDetailsMapper;
    public CartResponse toCartResponse(Cart cart) {
        return new CartResponse(cart.getUserId(), cart.getStatus(),cart.getDetails().stream().map(productDetailsMapper::toProductDetailsResponse).collect(Collectors.toList()));
    }
    public PurchaseRequest toPurchaseRequest(ProductDetails productDetails) {
        return new PurchaseRequest(productDetails.getProductId(), productDetails.getQuantity());

    }
}
