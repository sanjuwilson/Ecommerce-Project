package com.app.Order.service_order;

import com.app.Order.service_orderline.OrderLine;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Validated
@EntityListeners(AuditingEntityListener.class)
@Table(name="order_table")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String reference;
    @Column(nullable = false)
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    private Integer customerId;
    @OneToMany(mappedBy="order",cascade=CascadeType.ALL)
    private List<OrderLine> orderLines;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updatedAt;

}
