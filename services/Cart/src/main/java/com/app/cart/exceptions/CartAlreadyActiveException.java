package com.app.cart.exceptions;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@AllArgsConstructor

@EqualsAndHashCode(callSuper=true)
public class CartAlreadyActiveException extends Exception {
    private String message;
}
