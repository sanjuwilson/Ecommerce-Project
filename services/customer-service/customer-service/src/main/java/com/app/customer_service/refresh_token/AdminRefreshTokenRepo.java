package com.app.customer_service.refresh_token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface AdminRefreshTokenRepo extends JpaRepository<AdminRefreshToken, UUID> {
    AdminRefreshToken findByAdministrator_Id(Integer adminId);
}
