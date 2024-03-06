package com.databasecourse.erfan.web.util;

import java.security.SecureRandom;
import java.util.Random;

public class RandomDatabaseNameGenerator {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int NAME_LENGTH = 8;

    private static final Random random = new SecureRandom();

    public static String generateRandomDatabaseName() {
        StringBuilder sb = new StringBuilder(NAME_LENGTH);
        for (int i = 0; i < NAME_LENGTH; i++) {
            int randomIndex = random.nextInt(ALPHABET.length());
            char randomChar = ALPHABET.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

}
