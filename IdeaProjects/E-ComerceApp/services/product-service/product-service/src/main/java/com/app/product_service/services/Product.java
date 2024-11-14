package com.app.product_service.services;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class Product {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private double availableQuantity;
    private BigDecimal price;
    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;
}
