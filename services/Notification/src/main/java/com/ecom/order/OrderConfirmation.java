package com.ecom.order;

import com.ecom.TransactionMethod;
import com.ecom.notification.Notification;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderConfirmation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer order_id;
    private String orderReference;
    private BigDecimal totalAmount;
    @ElementCollection(targetClass = TransactionMethod.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "order_method_table",
            joinColumns = @JoinColumn(name = "order_conf_id")
    )
    @Column(name = "payment_method")
    private List<TransactionMethod> paymentMethod=new ArrayList<>();
    @OneToOne(mappedBy = "orderConfirmation",cascade = CascadeType.ALL)
    private Customer customer;
    @OneToMany(mappedBy = "orderConfirmation",cascade = CascadeType.ALL)
    private List<Product> products;
    @OneToOne
    @JoinColumn(name="notification_id")
    private Notification notification;
}
