package com.app.rewards_service.exceptions;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
public class OrderNotFoundException extends RuntimeException {
    private String message;

}
