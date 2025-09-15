package com.app.customer_service.customer;
import com.app.customer_service.admin.Administrator;
import com.app.customer_service.keycloak.CodeRequest;
import com.app.customer_service.keycloak.LogoutRequest;
import com.app.customer_service.keycloak.Roles;
import com.app.customer_service.refresh_token.AdminRefreshToken;
import com.app.customer_service.refresh_token.UserRefreshToken;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ecom.EmailRequest;
import com.ecom.RegisterAdminRequest;
import com.ecom.SuperAdminRequest;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.security.config.Elements.JWT;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/customer")
public class CustomerController {
    private final CustomerService service;

    @PostMapping("/register")
    public ResponseEntity<Integer> createCustomer(@RequestBody @Valid CustomerDto customer){
        return ResponseEntity.ok(service.save(customer));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody CodeRequest codeRequest) {
        JsonNode fullTokenResponse = service.exchangeCodeForTokens(codeRequest);
        if (fullTokenResponse == null || !fullTokenResponse.has("access_token")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token exchange failed"));
        }

        String accessToken = fullTokenResponse.get("access_token").asText();
        String refreshToken = fullTokenResponse.get("refresh_token").asText();
        int expiresIn = fullTokenResponse.get("expires_in").asInt();
        long refreshTokenExpiresIn = fullTokenResponse.get("refresh_expires_in").asLong();




        service.saveAndValidateTokens(accessToken,refreshToken,refreshTokenExpiresIn);
        return ResponseEntity.ok(Map.of(
                "access_token", accessToken,
                "expires_in", expiresIn
        ));
    }


    @PutMapping
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<Void> updateCustomer(@RequestBody @Valid CustomerUpdateDto customer,  @AuthenticationPrincipal Jwt jwt){
        String customerId = jwt.getClaimAsString("customer_id");
        service.updateCustomer(customer,Integer.parseInt(customerId));
        return ResponseEntity.accepted().build();
    }
    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<CustomerResponse>> getAllCustomer(){
        return ResponseEntity.ok(service.getAllCustomer());
    }
    @PreAuthorize("hasRole('admin')or hasRole('client_cart')")
    @GetMapping("existsById/{id}")
    public ResponseEntity<Boolean> getCustomer(@PathVariable("id") int cusId,@RequestHeader("Authorization")String token){
        return ResponseEntity.ok(service.isExisting(cusId));
    }
    @PreAuthorize("hasRole('admin')or hasRole('client_order')")
    @GetMapping("findById/{customerId}")
    public ResponseEntity<CustomerResponse> findById(@PathVariable("customerId") int customerId, @RequestHeader("Authorization")String authHeader){
        return ResponseEntity.ok(service.getById(customerId));
    }
    @DeleteMapping("deleteById/{customerId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("customerId") int cusId){
        service.deleteCustomer(cusId);
        return ResponseEntity.accepted().build();
    }
    @PreAuthorize("hasRole('super_admin')")
    @GetMapping("create/admin")
    public ResponseEntity<Void> createAdmin(@RequestBody SuperAdminRequest request){
        service.generateAndSendToken(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("register/admin_client")
    public ResponseEntity<Void> registerAdminClient(@RequestBody RegisterAdminRequest adminRequest){
        service.registerAdmin(adminRequest);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        service.logout(authHeader).block();
        return ResponseEntity.ok().build();
    }
    @PostMapping("/token/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestHeader("Authorization") String authHeader) {
        Map<String, Object> tokenResponse = service.refreshAccessToken(authHeader);
        return ResponseEntity.ok(tokenResponse);
    }
    @GetMapping("/dashboard")
    public ResponseEntity<String> getDashBoard(@RequestHeader("Authorization") String authHeader){
       return ResponseEntity.ok(service.fetchDashBoard(authHeader));

    }

}
