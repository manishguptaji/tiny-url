package com.learn.tinyurl.util;

import org.springframework.beans.factory.annotation.Value;

import java.security.SecureRandom;

public class ShortCodeGenerator {

    private static final String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom random = new SecureRandom();

    public static String generateShortCode() {
        StringBuilder shortCode = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            int index = random.nextInt(characters.length());
            shortCode.append(characters.charAt(index));
        }
        return shortCode.toString();
    }

}
