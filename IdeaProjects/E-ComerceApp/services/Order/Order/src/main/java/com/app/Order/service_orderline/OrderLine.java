package com.app.Order.service_orderline;

import com.app.Order.service_order.Order;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;
    @ManyToOne
    @JoinColumn(name = "orderId")
    private Order order;
    private Integer productId;
    private double quantity;
}
