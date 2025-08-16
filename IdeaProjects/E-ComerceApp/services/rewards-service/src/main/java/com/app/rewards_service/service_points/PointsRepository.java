package com.app.rewards_service.service_points;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface PointsRepository extends JpaRepository<PointsPayment,Integer> {
    Optional<PointsPayment> findTopByTransaction_UserIdOrderByIdDesc(Integer userId);



}
