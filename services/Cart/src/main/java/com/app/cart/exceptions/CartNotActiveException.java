package com.app.cart.exceptions;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@AllArgsConstructor

public class CartNotActiveException extends Exception {
    private String message;
}
