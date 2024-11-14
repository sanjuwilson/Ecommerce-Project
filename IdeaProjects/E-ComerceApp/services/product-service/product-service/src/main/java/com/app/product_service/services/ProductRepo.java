package com.app.product_service.services;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Integer> {
    List<Product> findAllByIdInOrderById(List<Integer> ids);

}
