package com.app.product_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;
    @PostMapping
    public ResponseEntity<Integer> save(@RequestBody ProductDto dto){
        return ResponseEntity.ok(service.save(dto));
    }
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll(){
        return ResponseEntity.ok(service.getAllProducts());
    }
    @GetMapping("/order")
    public ResponseEntity<List<ProductResponse>> requestOrder(List<PurchaseRequest> request){
        return ResponseEntity.ok(service.requestPurchase(request));
    }
    @GetMapping("/byId/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable("id") Integer Id){
        return ResponseEntity.ok(service.getProductsById(Id));

    }
}
