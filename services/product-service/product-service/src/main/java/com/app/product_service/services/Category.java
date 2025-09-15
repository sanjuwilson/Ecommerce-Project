package com.app.product_service.services;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class Category {
    @Id
    @GeneratedValue
    private Integer id;
    private String description;
    @OneToMany(mappedBy = "category",cascade= CascadeType.ALL)
    private List<Product> products;
}
