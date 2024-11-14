package com.app.customer_service.customer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/customer")
public class CustomerController {
    private final CustomerService service;

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody @Valid CustomerDto customer){
        return ResponseEntity.ok(service.save(customer));
    }
    @PutMapping
    public ResponseEntity<Void> updateCustomer(@RequestBody @Valid CustomerDto customer){
        service.updateCustomer(customer);
        return ResponseEntity.accepted().build();
    }
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomer(){
        return ResponseEntity.ok(service.getAllCustomer());
    }
    @GetMapping("exitsById/{id}")
    public ResponseEntity<Boolean> getCustomer(@PathVariable("id") int cusId){
        return ResponseEntity.ok(service.isExisting(cusId));
    }
    @GetMapping("findById/{customerId}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable("customerId") int cusId){
        return ResponseEntity.ok(service.getById(cusId));
    }
    @DeleteMapping("deleteById/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("customerId") int cusId){
        service.deleteCustomer(cusId);
        return ResponseEntity.accepted().build();
    }
}
