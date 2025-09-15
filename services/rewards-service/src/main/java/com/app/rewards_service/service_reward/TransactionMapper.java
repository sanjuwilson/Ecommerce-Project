package com.app.rewards_service.service_reward;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionMapper {
    public Transaction toTransaction(TransactionRequest transactionRequest) {
         return Transaction.builder().orderReference(transactionRequest.orderReference()).build();
    }
}
