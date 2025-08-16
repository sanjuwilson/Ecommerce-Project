package com.app.cart.service_cart;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public  class CartReferenceGenerator {

    private static final SecureRandom random = new SecureRandom();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int RANDOM_SUFFIX_LENGTH = 6;

    // Format: ORD-20250722-ABC123
    public static String generateCartReference() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomSuffix = generateRandomSuffix(RANDOM_SUFFIX_LENGTH);
        return "CART-" + timestamp + "-" + randomSuffix;
    }

    private static String generateRandomSuffix(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}

