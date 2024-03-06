package com.databasecourse.erfan.web.util;

import java.util.regex.Pattern;

public class MySQLNamesValidator {

    public static boolean isValidMySQLUsername(String username) {
        // Define the regular expression pattern for a valid MySQL username
        String regex = "^(?!\\d)(?!.*_$)[A-Za-z\\d_$]{1,16}$";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(regex);

        // Use Matcher to check if the username matches the pattern
        return pattern.matcher(username).matches();
    }

    public static boolean isValidMySQLDatabaseName(String databaseName) {
        // Define the regular expression pattern for a valid MySQL database name
        String regex = "^(?!\\d)[A-Za-z\\d_$]{1,64}$";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(regex);

        // Use Matcher to check if the database name matches the pattern
        return pattern.matcher(databaseName).matches();
    }

}
