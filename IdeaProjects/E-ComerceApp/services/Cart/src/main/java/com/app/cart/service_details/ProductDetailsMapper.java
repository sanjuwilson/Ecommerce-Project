package com.app.cart.service_details;

import com.app.cart.service_cart.Cart;
import com.app.cart.service_order.PurchaseRequest;
import org.springframework.stereotype.Service;

@Service
public class ProductDetailsMapper {
    public ProductDetails toProductDetails(ProductDetailsRequest detailsRequest) {
        return ProductDetails.builder().productId(detailsRequest.productId()).quantity(detailsRequest.quantity())
                .cart(Cart.builder().build())
                .build();
    }
    public ProductDetailsResponse toProductDetailsResponse(ProductDetails productDetails) {
        return new ProductDetailsResponse(productDetails.getProductId(), productDetails.getQuantity());
    }

}
