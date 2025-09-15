package com.app.cart.service_details;

import com.app.cart.service_cart.Cart;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProductDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    Integer productId;
    double quantity;
    @ManyToOne
    @JoinColumn(name="cart_id")
    Cart cart;
}
