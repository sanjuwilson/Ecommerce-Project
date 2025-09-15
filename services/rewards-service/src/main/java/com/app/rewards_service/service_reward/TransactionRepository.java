package com.app.rewards_service.service_reward;

import com.app.rewards_service.service_points.PointsPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    boolean existsByOrderReference(String orderReference);


}
