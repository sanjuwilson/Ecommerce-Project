package com.app.rewards_service.service_reward;

import com.ecom.TransactionMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentMethodDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ElementCollection(targetClass = TransactionMethod.class)
    @CollectionTable(name = "transaction_payment_methods", joinColumns = @JoinColumn(name = "payment_details_id"))
    @Enumerated(EnumType.STRING)
    private Set<TransactionMethod> paymentMethod;
    private BigDecimal amountFromCreditCard;
    private BigDecimal amountFromDebitCard;
    private BigDecimal amountFromPoints;
    private BigDecimal amountFromGiftCard;
    @OneToOne
    @JoinColumn(name="transaction_id")
    private Transaction transaction;
}
