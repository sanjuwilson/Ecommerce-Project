package com.app.customer_service.address;


import com.app.customer_service.customer.Customer;
import jakarta.persistence.*;
import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String street;
    private String houseNumber;
    private int zipCode;
    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;
}
