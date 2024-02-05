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
 *     Author: Rgouaud
 *     Version: 1.0
 *     </p>
 */
public class DateValidator {

    /**
     * The regular expression for the date format "dd/MM/yyyy".
     */
    private static final String regex = "^(0[1-9]|[12]\\d|3[01])/(0[1-9]|1[0-2])/(\\d{4})$";

    /**
     * The pattern for the date format "dd/MM/yyyy".
     */
    private static final Pattern DATE_PATTERN = Pattern.compile(regex);

    /**
     * Validates a date string based on the format "dd/MM/yyyy."
     *
     * @param dateStr The date string to be validated.
     * @return True if the date string is valid, false otherwise.
     */
    public static boolean isValid(String dateStr) {
        // compare dateStr with regex
        Matcher matcher = DATE_PATTERN.matcher(dateStr);
        return matcher.matches();
    }
}
