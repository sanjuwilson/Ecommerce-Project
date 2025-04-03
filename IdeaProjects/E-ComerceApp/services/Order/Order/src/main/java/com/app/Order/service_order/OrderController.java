package com.app.Order.service_order;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;

    @PostMapping ("/place-order")
    public ResponseEntity<Integer> placeOrder(@RequestBody @Valid OrderRequest request) {
        return ResponseEntity.ok(service.placeOrder(request));
    }
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders() {
        return ResponseEntity.ok(service.findAll());
    }
    @GetMapping("/{order-id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable("order-id") int orderId) {
        return ResponseEntity.ok(service.getById(orderId));
    }
    @DeleteMapping("/{order-id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("order-id") int orderId) {
      service.deleteById(orderId);
      return ResponseEntity.noContent().build();
    }

}
