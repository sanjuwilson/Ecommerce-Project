package com.app.Order.service_product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name="product",url="${application.config.product-url}")

public interface ProductClient {
    @GetMapping("/byId/{id}")
    public Optional<ProductResponse> getById(@PathVariable("id") Integer Id);
}
