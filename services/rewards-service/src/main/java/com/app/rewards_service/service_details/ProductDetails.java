package com.app.rewards_service.service_details;
import com.app.rewards_service.service_reward.Transaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer productId;
    private double quantity;
    private int point;
    @ManyToOne
    @JoinColumn(name="transcation_id")
    private Transaction transaction;
    private double cashPerPoint;
}
