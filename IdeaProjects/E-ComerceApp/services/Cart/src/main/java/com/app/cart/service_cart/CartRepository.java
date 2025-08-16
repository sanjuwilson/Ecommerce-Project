package com.app.cart.service_cart;

import com.ecom.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Integer countByUserIdAndStatus(int userId, CartStatus status);
    Cart findByCartReference(String cartReference);
    Optional<Cart >findByUserId(Integer userId);

}
