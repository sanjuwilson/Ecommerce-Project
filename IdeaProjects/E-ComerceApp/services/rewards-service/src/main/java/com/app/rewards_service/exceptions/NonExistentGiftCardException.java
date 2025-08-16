package com.app.rewards_service.exceptions;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
public class NonExistentGiftCardException extends RuntimeException {
    String message;
}
