package com.app.rewards_service.service_gift_cards.code;

import java.security.SecureRandom;
import java.util.Base64;

public class CodeGenerator {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final String ALPHANUMERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String generateSecureCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = secureRandom.nextInt(ALPHANUMERIC.length());
            sb.append(ALPHANUMERIC.charAt(index));
        }
        return sb.toString();
    }
}
