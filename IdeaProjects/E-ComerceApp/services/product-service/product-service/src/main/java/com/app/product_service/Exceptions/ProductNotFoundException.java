package com.app.product_service.Exceptions;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
public class ProductNotFoundException extends RuntimeException {
    private String msg;
}
