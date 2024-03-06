package com.databasecourse.erfan.web.util;

import java.util.Random;

public class PasswordGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()";
    private static final String NUMBERS = "0123456789";

    public static String generateRandomPassword(int length) {
        Random random = new Random();
        StringBuilder password = new StringBuilder();



        for (int i = 0; i < length-2; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            password.append(randomChar);
        }

        // special char
        int randomIndex = random.nextInt(SPECIAL_CHARACTERS.length());
        char randomChar = SPECIAL_CHARACTERS.charAt(randomIndex);
        password.append(randomChar);

        //number
         randomIndex = random.nextInt(NUMBERS.length());
         randomChar = NUMBERS.charAt(randomIndex);
        password.append(randomChar);

        return password.toString();
    }

}
