package com.app.rewards_service.service_points;
import com.app.rewards_service.service_reward.Transaction;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class PointsPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private int currentPointsAfterTransaction;
    private int pointsUsed;
    private int pointsEarned;
    private double cashEarned;
    private double totalCashAfterTransaction;
    @OneToOne
    @JoinColumn(name="transaction_id")
    private Transaction transaction;



}
