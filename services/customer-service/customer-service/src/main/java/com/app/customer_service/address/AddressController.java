package com.app.customer_service.address;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;
    @GetMapping
    public ResponseEntity<List<AddressResponse>> getAllAddresses() {
        return ResponseEntity.ok(addressService.getAll());
    }
    @GetMapping("/{customerId}")
    public ResponseEntity<List<AddressResponse>> getAddressCustomerId(@PathVariable("customerId") Integer id) {
        return ResponseEntity.ok(addressService.getByCustomerId(id));

    }
    @PutMapping("/{addressId}")
    public ResponseEntity<Void> updateAddress(@RequestBody AddressRequest addressRequest,@PathVariable("addressId") Integer id) {
        addressService.update(id,addressRequest);
        return ResponseEntity.accepted().build();
    }
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable("addressId") Integer id) {
        addressService.delete(id);
        return ResponseEntity.accepted().build();
    }

}
