package com.ecom.notification;

import com.ecom.order.OrderConfirmation;
import com.ecom.payment.PaymentConfirmation;
import com.ecom.point.PointDetailsConfirmation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
    @CreatedDate
    private LocalDateTime notificationTime;
    @OneToOne(mappedBy = "notification",cascade = CascadeType.ALL)
    private OrderConfirmation orderConfirmation;
    @OneToOne(mappedBy = "notification",cascade = CascadeType.ALL)
    private PaymentConfirmation paymentConfirmation;
    @OneToOne(mappedBy = "notification",cascade = CascadeType.ALL)
    private PointDetailsConfirmation pointDetailsConfirmation;


}
