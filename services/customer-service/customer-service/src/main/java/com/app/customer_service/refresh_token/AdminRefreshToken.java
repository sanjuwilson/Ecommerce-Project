package com.app.customer_service.refresh_token;

import com.app.customer_service.admin.Administrator;
import com.app.customer_service.customer.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
@Builder
public class AdminRefreshToken {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "encrypted_refresh_token", nullable = false, length = 2048)
    private String encryptedRefreshToken;
    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private Instant createdAt;
    @Column(name = "updated_at")
    @LastModifiedDate
    private Instant updatedAt;
    @Column(name = "expires_at")
    private Instant expiresAt;
    @Column(name = "keycloak_session_id")
    private String keycloakSessionId;
    @OneToOne
    @JoinColumn(name="admin_id")
    private Administrator administrator;
}
