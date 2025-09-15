package com.app.rewards_service.service_reward;

import com.app.rewards_service.service_details.ProductDetails;
import com.app.rewards_service.service_details.ProductDetailsDto;
import com.app.rewards_service.service_points.PointsPayment;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public record TransactionRequest(
        @NotBlank(message = "Order reference is required")
        String orderReference,
        Integer pointsUsed,
        String giftCardCode,
        @DecimalMin(value = "0.0", message = "Amount from gift card must be positive")
        BigDecimal amountFromGiftCard,
        @DecimalMin(value = "0.0", message = "Amount from debit card must be positive")
        BigDecimal amountFromDebit,
        @DecimalMin(value = "0.0", message = "Amount from credit card must be positive")
        BigDecimal amountFromCredit
        ){
}
