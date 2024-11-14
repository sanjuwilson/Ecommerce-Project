package com.app.Order.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data
public class BuissnessException extends RuntimeException{
    private final String message;
}
