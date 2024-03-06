package com.databasecourse.erfan.web.util;

public class TimeStringMinuteFunctions {

    public static String formatDuration(double minutes) {
        long totalMinutes = (long) minutes; // Truncate the decimal part to get the whole minutes
        long days = totalMinutes / (24 * 60); // 24 hours in a day, 60 minutes in an hour
        long hours = (totalMinutes % (24 * 60)) / 60; // Remaining hours after removing days
        long minutesPart = totalMinutes % 60; // Remaining minutes after removing days and hours
        double seconds = (minutes - (double) totalMinutes) * 60; // Calculate seconds from the decimal part

        // Create the formatted string with indicators
        String formattedDuration = String.format("%dd %02dh %02dm %02.0fs", days, hours, minutesPart, seconds);

        return formattedDuration;
    }

    public static double parseFormattedDuration(String formattedDuration) {
        String[] parts = formattedDuration.split(" ");
        double totalMinutes = 0.0;

        for (String part : parts) {
            char indicator = part.charAt(part.length() - 1); // Get the last character as the indicator
            double value = Double.parseDouble(part.substring(0, part.length() - 1)); // Get the numeric part

            switch (indicator) {
                case 'd':
                    totalMinutes += value * 24 * 60; // Convert days to minutes
                    break;
                case 'h':
                    totalMinutes += value * 60; // Convert hours to minutes
                    break;
                case 'm':
                    totalMinutes += value; // Minutes
                    break;
                case 's':
                    totalMinutes += value / 60; // Convert seconds to minutes
                    break;
            }
        }

        return totalMinutes;
    }

}
