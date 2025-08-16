package com.app.rewards_service.service_order;

import com.app.rewards_service.service_reward.TransactionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="order",url = "http://localhost:9090/api/v1/orders")
public interface OrderClient {
    @GetMapping("/get-transactions")
    TransactionResponse getTransactionDetails(@RequestParam String reference, @RequestHeader("Authorization")String token);
}
