package com.app.cart.exceptions;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
public class CartAlreadyCheckedOutException extends Exception {
    private String message;
}
