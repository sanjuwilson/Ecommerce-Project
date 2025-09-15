package com.app.rewards_service.service_points;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PointsService {
    private final PointsRepository pointsRepository;
    public Optional<PointsPayment> getPointsDetailsByUserId(Integer userId) {
        return pointsRepository.findTopByTransaction_UserIdOrderByIdDesc(userId);

    }

}
