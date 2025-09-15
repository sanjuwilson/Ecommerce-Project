package com.app.customer_service.exceptions;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
public class CustomerNotFoundException extends RuntimeException {
   private String message;
}
