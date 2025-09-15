package com.app.customer_service.refresh_token;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRefreshTokenRepo extends JpaRepository<UserRefreshToken, UUID> {
    UserRefreshToken findByCustomer_Id(Integer customerId);
}
