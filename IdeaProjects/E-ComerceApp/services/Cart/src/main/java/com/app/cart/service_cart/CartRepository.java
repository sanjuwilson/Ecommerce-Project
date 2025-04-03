package com.app.cart.service_cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Integer countByUserIdAndStatus(int userId, CartStatus status);

}
