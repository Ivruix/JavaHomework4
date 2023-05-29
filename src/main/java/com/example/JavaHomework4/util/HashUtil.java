package com.example.JavaHomework4.util;

import org.springframework.stereotype.Component;
import java.security.MessageDigest;

@Component
public class HashUtil {
    private static final String ALGORITHM = "SHA-256";

    // Метод для хеширования пароля
    public String hashPassword(String password) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(ALGORITHM);
        } catch (Exception ignored) {
        }
        assert messageDigest != null;
        messageDigest.update(password.getBytes());
        return bytesToHex(messageDigest.digest());
    }

    // Метод для преобразования массива байт в шестнадцатеричное представление
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }
}
