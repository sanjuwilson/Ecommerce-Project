package com.app.rewards_service.service_gift_cards.giftcard_transactioninfo;

import com.app.rewards_service.service_gift_cards.GiftCard;
import com.app.rewards_service.service_reward.Transaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public class GiftCardTransactionDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @OneToOne
    @JoinColumn(name="transaction_id")
    Transaction transaction;
    BigDecimal amountUsed;
    @CreatedDate
    LocalDateTime transactionDate;
    @ManyToOne
    @JoinColumn(name="giftcard_id")
    GiftCard giftCard;
}
