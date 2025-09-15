package com.app.rewards_service.service_gift_cards;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record GiftCardPurchaseRequest(@NotNull Integer userId, @NotNull @Min(10) @Max(5000) BigDecimal amount, @Email @NotNull String email) {
}
