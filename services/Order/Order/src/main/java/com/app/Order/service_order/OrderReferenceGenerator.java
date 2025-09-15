package com.app.Order.service_order;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public  class OrderReferenceGenerator {

    private static final SecureRandom random = new SecureRandom();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int RANDOM_SUFFIX_LENGTH = 6;

    // Format: ORD-20250722-ABC123
    public static String generateOrderReference() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomSuffix = generateRandomSuffix(RANDOM_SUFFIX_LENGTH);
        return "ORD-" + timestamp + "-" + randomSuffix;
    }

    private static String generateRandomSuffix(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}

