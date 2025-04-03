package com.app.Order.service_product;

import com.app.Order.service_order.PurchaseRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@FeignClient(name="product",url="${application.config.product-url}")

public interface ProductClient {
    @PostMapping("/order-request")
    public List<ProductResponse> requestOrder(@RequestBody List<PurchaseRequest> request);
}
