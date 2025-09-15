package com.app.Order.service_customer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Optional;

@FeignClient(name="customer",url="${application.config.customer-url}")
public interface CustomerClient {
    @GetMapping("findById/{customerId}")
    public Optional<CustomerResponse> findById(@PathVariable("customerId") int customerId, @RequestHeader("Authorization")String authHeader);


}
