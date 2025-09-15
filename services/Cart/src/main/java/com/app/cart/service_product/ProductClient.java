package com.app.cart.service_product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="product",url="${application.config.product-url}")
public interface ProductClient {
    @GetMapping("/byId/{id}")
    public ProductResponse getById(@PathVariable("id") Integer Id,@RequestHeader("Authorization") String token);
}
