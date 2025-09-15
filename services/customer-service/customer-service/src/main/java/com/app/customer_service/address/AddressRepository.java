package com.app.customer_service.address;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    List<Address> findAllByCustomerId(int customerId);
    Address findById(int id);
}
