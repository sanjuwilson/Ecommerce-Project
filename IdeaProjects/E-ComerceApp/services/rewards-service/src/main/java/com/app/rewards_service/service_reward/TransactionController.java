package com.app.rewards_service.service_reward;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/reward")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    @PreAuthorize("hasRole('user')")
    @PostMapping
    public ResponseEntity<Integer> addTransaction(@RequestBody TransactionRequest transaction) {
        return ResponseEntity.ok(transactionService.saveTransaction(transaction));
    }

}
