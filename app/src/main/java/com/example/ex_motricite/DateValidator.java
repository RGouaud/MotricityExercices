package com.example.ex_motricite;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code DateValidator} class provides a helper for validating date strings.
 * It includes a method, {@link #isValid(String)}, to check if a given date string is valid.
 *
 * <p>
 * The class uses a regular expression to compare the date string with the expected format "dd/MM/yyyy".
 * </p>
 *
 * <p>
 * Author: Rgouaud
 * Version: 1.0
 * </p>
 */
public class DateValidator {

    /**
     * Prevents instantiation of the {@code DateValidator} class.
     */
    private DateValidator() {
    }

    /**
     * The regular expression for the date format "dd/MM/yyyy".
     */
    private static final String REGEX = "^(0[1-9]|[12]\\d|3[01])/(0[1-9]|1[0-2])/(\\d{4})$";

    /**
     * The pattern for the date format "dd/MM/yyyy".
     */
    private static final Pattern DATE_PATTERN = Pattern.compile(REGEX);

    /**
     * Validates a date string based on the format "dd/MM/yyyy."
     *
     * @param dateStr The date string to be validated.
     * @return True if the date string is valid, false otherwise.
     */
    public static boolean isValid(String dateStr) {
        // compare dateStr with regex
        Matcher matcher = DATE_PATTERN.matcher(dateStr);
        boolean regex = matcher.matches();
        if (!regex) { // if dateStr doesn't match regex
            return false;
        }

        // slice dateStr to get day, month and year
        String[] date = dateStr.split("/");
        int day = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int year = Integer.parseInt(date[2]);

        if (month == 2) {
            return februaryValidator(day, month, year);
        }
        if (is30daysMonth(month)) {
            return day <= 30;
        }

        return true;
    }

    /**
     * Checks if a given year is a leap year.
     * @param year The year to be checked.
     * @return boolean
     */
    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    /**
     * Checks if a given month has 30 days.
     *
     * @param month The month to be checked.
     * @return boolean boolean
     */
    public static boolean is30daysMonth(int month) {
        return month == 4 || month == 6 || month == 9 || month == 11;
    }

    /**
     * Validates the day of February.
     *
     * @param day   The day to be validated.
     * @param month The month to be validated.
     * @param year  The year to be validated.
     * @return boolean boolean
     */
    public static boolean februaryValidator(int day, int month, int year) {
        return (day <= 29 && month == 2 && isLeapYear(year))
                || (day <= 28 && month == 2 && !isLeapYear(year));
    }
}
