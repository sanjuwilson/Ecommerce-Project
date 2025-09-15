package com.app.product_service.Exceptions;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
public class OutOfStockException extends RuntimeException {
    private String msg;
}
