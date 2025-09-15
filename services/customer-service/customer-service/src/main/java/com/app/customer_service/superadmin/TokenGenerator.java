package com.app.customer_service.superadmin;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
@Component
@RequiredArgsConstructor
public class TokenGenerator {
    @Value("${jwt.secret}")
    private String base64Secret;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        byte[] decodedKey = Base64.getDecoder().decode(base64Secret);
        this.secretKey = Keys.hmacShaKeyFor(decodedKey);
    }

    public String generateToken(String email,String firstName,String lastName) {
        long expirationMillis = 1000 * 60 * 15; // 15 minutes
        return Jwts.builder()
                .setSubject("admin_invite")
                .claim("email", email)
                .claim("first_name", firstName)
                .claim("second_name", lastName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}