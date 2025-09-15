package com.app.rewards_service.service_reward;

import com.app.rewards_service.service_details.ProductDetails;
import com.app.rewards_service.service_gift_cards.GiftCard;
import com.app.rewards_service.service_gift_cards.giftcard_transactioninfo.GiftCardTransactionDetails;
import com.app.rewards_service.service_points.PointsPayment;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class Transaction {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer transactionId;
    @Column(nullable = false)
    private int userId;
    @Column(nullable = false)
    private String orderReference;
    @OneToMany(mappedBy = "transaction",cascade = CascadeType.ALL)
    private List<ProductDetails>details;
    @CreatedDate
    @Column(updatable = false)
    private LocalDate transactionDate;
    @OneToOne(mappedBy="transaction",cascade = CascadeType.ALL)
    private PointsPayment pointsPayment;
    private BigDecimal totalPayableAmount;
    @OneToOne(mappedBy = "transaction",cascade = CascadeType.ALL)
    private GiftCardTransactionDetails giftCardTransactionDetails;
    @OneToOne(mappedBy = "transaction",cascade = CascadeType.ALL)
    private PaymentMethodDetails paymentMethodDetails;



}
