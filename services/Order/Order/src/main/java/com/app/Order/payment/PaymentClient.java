package com.app.Order.payment;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="payment",url="${application.config.payment-url}")
public interface PaymentClient {
    @PostMapping
    Integer requestPayment(@RequestBody @Valid PaymentRequest paymentRequest);
}
