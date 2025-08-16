package com.app.cart.service_order;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="order",url = "${application.config.order-url}")
public interface OrderClient {
    @PostMapping("/place-order")
    public Integer placeOrder(@RequestBody @Valid OrderRequest request, @RequestHeader("Authorization") String token);
}
