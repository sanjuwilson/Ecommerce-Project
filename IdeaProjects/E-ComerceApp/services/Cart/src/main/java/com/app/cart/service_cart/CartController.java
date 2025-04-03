package com.app.cart.service_cart;

import com.app.cart.exceptions.CartAlreadyActiveException;
import com.app.cart.exceptions.CartAlreadyCheckedOutException;
import com.app.cart.exceptions.CartNotActiveException;
import com.app.cart.service_details.ProductDetailsRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/cart")
public class CartController {
    private final CartService cartService;
    @PostMapping
    public ResponseEntity<Integer> createCart(@RequestBody @Valid CartRequest request) throws CartAlreadyActiveException {
        Integer cartId = cartService.saveCart(request);
        URI location = URI.create(String.format("/api/v1/cart/%d", cartId));
        return ResponseEntity.created(location).body(cartId);
    }
    @PostMapping("/{cart-id}")
    public ResponseEntity<Integer> addItemToCart(@RequestBody @Valid List<ProductDetailsRequest> request, @PathVariable("cart-id") Integer cartId) throws CartNotActiveException {
        return ResponseEntity.ok(cartService.addToCart(request,cartId));
    }
    @GetMapping("check-out/{cart-id}")
    public ResponseEntity<Void> checkoutCart(@PathVariable("cart-id")Integer cartId,@RequestBody CheckOutRequest checkOutRequest) throws CartAlreadyCheckedOutException {
        cartService.checkOutCart(cartId,checkOutRequest);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{cart-id}")
    public ResponseEntity<Void> deleteCart(@RequestBody List<ProductDetailsRequest> requests,@PathVariable("cart-id") Integer cartId) {
        cartService.deleteProductsFromCart(requests,cartId);
        return ResponseEntity.accepted().build();

    }






}
