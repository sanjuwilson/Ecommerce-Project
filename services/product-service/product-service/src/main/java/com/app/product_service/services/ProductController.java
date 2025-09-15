package com.app.product_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;
    @PreAuthorize("hasRole('admin')")
    @PostMapping
    public ResponseEntity<Integer> save(@RequestBody ProductDto dto){
        return ResponseEntity.ok(service.save(dto));
    }
    @PreAuthorize("hasRole('admin')")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll(){
        return ResponseEntity.ok(service.getAllProducts());
    }
    @PreAuthorize("hasRole('client_order')")
    @PostMapping("/order-request")
    public List<ProductResponse> requestOrder(@RequestBody List<PurchaseRequest> request,@RequestHeader("Authorization")String token){
        System.out.println("Received request: " + request);
        return service.requestPurchase(request);
    }
    @PreAuthorize("hasRole('admin') or hasRole('client_cart')")
    @GetMapping("/byId/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable("id") Integer Id,@RequestHeader("Authorization") String token){
        return ResponseEntity.ok(service.getProductsById(Id));

    }
}
