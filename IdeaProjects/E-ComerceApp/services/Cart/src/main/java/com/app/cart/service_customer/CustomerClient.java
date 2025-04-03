package com.app.cart.service_customer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="customer",url="${application.config.customer-url}")
public interface CustomerClient {
    @GetMapping("exitsById/{id}")
    boolean getCustomer(@PathVariable("id") int cusId);
}
