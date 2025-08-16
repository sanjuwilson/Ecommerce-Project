package com.app.Order.service_order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findOrderByReference(String reference);

    Order findByCartReference(String cartReference);

    Void deleteOrderByReference(String reference);
}
