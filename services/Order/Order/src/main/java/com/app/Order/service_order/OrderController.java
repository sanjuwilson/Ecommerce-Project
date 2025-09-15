package com.app.Order.service_order;

import com.app.Order.service_transaction.TransactionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;
    @PreAuthorize("hasRole('user') or hasRole('client_cart')")
    @PostMapping ("/place-order")
    public ResponseEntity<Integer> placeOrder(@RequestBody @Valid OrderRequest request, @RequestHeader("Authorization") String token, @AuthenticationPrincipal Jwt jwt) {
        Integer finalCustomerId=null;
        if(jwt.getClaimAsString("customer_id")!=null){
            finalCustomerId = Integer.parseInt(jwt.getClaimAsString("customer_id"));
        }
        else{
            finalCustomerId= request.customerId();
        }
        return ResponseEntity.ok(service.placeOrder(request,finalCustomerId));
    }
    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<OrderResponse>> getOrders() {
        return ResponseEntity.ok(service.findAll());
    }
    @GetMapping("/{order-id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<OrderResponse> findById(@PathVariable("order-id") int orderId) {
        return ResponseEntity.ok(service.getById(orderId));
    }
    @DeleteMapping("/{order-reference}")
    @PreAuthorize("hasRole('admin') or hasRole('user')")
    public ResponseEntity<Void> deleteOrder(@PathVariable("order-reference") String orderReference) {
      service.deleteById(orderReference);
      return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasRole('client_transaction')")
    @GetMapping("/get-transactions")
    TransactionResponse getTransactionDetails(@RequestParam String reference, @RequestHeader("Authorization")String token) {
        return service.getByReference(reference);
    }

}
