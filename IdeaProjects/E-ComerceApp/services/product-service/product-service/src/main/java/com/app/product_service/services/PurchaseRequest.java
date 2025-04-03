package com.app.product_service.services;

import jakarta.validation.constraints.Min;
import lombok.NonNull;

public record PurchaseRequest(Integer id, @Min(1) int quantity){
}
