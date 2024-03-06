package com.databasecourse.erfan.web.util;

import com.databasecourse.erfan.Constants;

public class StringTruncator {
    public static String truncateString(String input) {
        int maxLength = Constants.MAX_TEXT_CONTENT_LENGTH;
        if (input.length() <= maxLength) {
            return input;
        } else {
            return input.substring(0, maxLength) + "...";
        }
    }
}
