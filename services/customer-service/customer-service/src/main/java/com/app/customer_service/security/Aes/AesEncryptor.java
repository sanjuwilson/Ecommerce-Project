package com.app.customer_service.security.Aes;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class AesEncryptor {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    @Value("${encryption.secret}")
    private String secret;

    private SecretKeySpec secretKeySpec;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        secretKeySpec = new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public String aesEncrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            throw new RuntimeException("Error while encrypting refresh token", ex);
        }
    }

    public String aesDecrypt(String encryptedBase64) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedBase64));
            return new String(decryptedBytes);
        } catch (Exception ex) {
            throw new RuntimeException("Error while decrypting refresh token", ex);
        }
    }
}
