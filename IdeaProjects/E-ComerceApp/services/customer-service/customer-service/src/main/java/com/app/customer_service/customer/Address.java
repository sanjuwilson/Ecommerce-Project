package com.app.customer_service.customer;

import jakarta.persistence.Embeddable;
import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class Address {
    private String street;
    private String houseNumber;
    private int zipCode;
}
