package com.app.customer_service.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AdminRepo extends JpaRepository<Administrator, Integer> {
    Optional<Administrator> findByEmail(String email);
    Boolean existsByEmail(String email);

}
