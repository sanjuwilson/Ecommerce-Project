package com.app.rewards_service.service_gift_cards;
import com.app.rewards_service.service_gift_cards.giftcard_transactioninfo.GiftCardTransactionDetails;
import jakarta.persistence.Entity;

import com.app.rewards_service.service_reward.Transaction;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiftCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true, nullable = false)
    private String code;
    @Column(nullable = false)
    private BigDecimal initialAmount;
    @Column(nullable = false)
    private BigDecimal remainingBalance;
    private LocalDateTime expiryDate;
    @Enumerated(EnumType.STRING)
    private GiftCardStatus status;
    @OneToOne(mappedBy="giftCard",cascade = CascadeType.ALL)
    private GiftCardPurchaseRecord giftCardPurchaseRecord;
    @OneToMany(mappedBy = "giftCard",cascade = CascadeType.ALL)
    private List<GiftCardTransactionDetails>giftCardTransactions;

}
