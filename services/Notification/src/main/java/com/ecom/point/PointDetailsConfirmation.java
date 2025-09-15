package com.ecom.point;

import com.ecom.notification.Notification;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PointDetailsConfirmation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private int totalPoints;
    private int pointsEarned;
    private double equivalentCashEarned;
    private String orderReference;
    private String customerName;
    private String customerEmail;
    @OneToOne
    @JoinColumn(name="notification_id")
    private Notification notification;
}
