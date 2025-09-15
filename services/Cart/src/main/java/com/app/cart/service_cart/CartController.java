package com.app.cart.service_cart;

import com.app.cart.exceptions.CartAlreadyActiveException;
import com.app.cart.exceptions.CartAlreadyCheckedOutException;
import com.app.cart.exceptions.CartNotActiveException;
import com.app.cart.service_details.ProductDetailsRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/cart")
public class CartController {
    private final CartService cartService;
    @PostMapping
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<Integer> createCart(@RequestBody @Valid CartRequest request,@AuthenticationPrincipal Jwt jwt) throws CartAlreadyActiveException {
        String customerId = jwt.getClaimAsString("customer_id");
        Integer cartId = cartService.saveCart(request,Integer.parseInt(customerId));
        URI location = URI.create(String.format("/api/v1/cart/%d", cartId));
        return ResponseEntity.created(location).body(cartId);
    }
    @PostMapping("/add")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<Integer> addItemToCart(@RequestBody @Valid List<ProductDetailsRequest> request, @AuthenticationPrincipal Jwt jwt) throws CartNotActiveException {
        String customerId = jwt.getClaimAsString("customer_id");
        return ResponseEntity.ok(cartService.addToCart(request,(Integer.parseInt(customerId))));
    }
    @GetMapping("check-out")
    @PreAuthorize("hasRole('user') or hasRole('admin')")
    public ResponseEntity<Void> checkoutCart(@AuthenticationPrincipal Jwt jwt) throws CartAlreadyCheckedOutException {
        Integer customerId=Integer.parseInt(jwt.getClaimAsString("customer_id"));
        cartService.checkOutCart(customerId);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('user') or hasRole('admin')")
    public ResponseEntity<Void> deleteCart(@RequestBody List<ProductDetailsRequest> requests, @AuthenticationPrincipal Jwt jwt) throws CartNotActiveException {
        cartService.deleteProductsFromCart(requests,Integer.parseInt(jwt.getClaimAsString(("customer_id"))));
        return ResponseEntity.accepted().build();

    }






}
