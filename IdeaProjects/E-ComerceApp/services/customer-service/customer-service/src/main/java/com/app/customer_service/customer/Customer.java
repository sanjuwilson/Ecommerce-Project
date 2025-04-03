package com.app.customer_service.customer;

import com.app.customer_service.address.Address;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
public class Customer {
    @Id
    @GeneratedValue
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private List<Address> addresses;

}
