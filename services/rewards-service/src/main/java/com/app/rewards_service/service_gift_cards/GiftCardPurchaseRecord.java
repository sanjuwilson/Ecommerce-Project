package com.app.rewards_service.service_gift_cards;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
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
public class GiftCardPurchaseRecord {
    @Id
    @GeneratedValue
    private Integer id;
    private BigDecimal amount;
    private Integer purchaserUserId;
    private String recipientEmail;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime issuedAt;
    @OneToOne
    @JoinColumn(name="card_id")
    private GiftCard giftCard;
}
