package com.app.rewards_service.service_details;

import com.app.rewards_service.service_reward.Transaction;

import java.io.Serializable;

public record ProductDetailsDto(Integer productId, Integer quantity,Transaction transaction)  {
}
