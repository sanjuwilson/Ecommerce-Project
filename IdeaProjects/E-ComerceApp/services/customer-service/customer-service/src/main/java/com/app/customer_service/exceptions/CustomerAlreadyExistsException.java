package com.app.customer_service.exceptions;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
public class CustomerAlreadyExistsException extends RuntimeException {
    private String message;

}
