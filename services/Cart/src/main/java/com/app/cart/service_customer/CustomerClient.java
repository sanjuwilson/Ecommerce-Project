package com.app.cart.service_customer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="customer",url="${application.config.customer-url}")
public interface CustomerClient {
    @GetMapping("existsById/{id}")
    boolean getCustomer(@PathVariable("id") int cusId,@RequestHeader("Authorization") String token);
}
