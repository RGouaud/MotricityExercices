package com.example.ex_motricite;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateValidator {

    // date regex "dd/MM/yyyy"
    private static final String regex = "^(0[1-9]|[12]\\d|3[01])/(0[1-9]|1[0-2])/(\\d{4})$";
    private static final Pattern DATE_PATTERN = Pattern.compile(regex);
    public static boolean isValid(String dateStr) {
        // compare dateStr with regex
        Matcher matcher = DATE_PATTERN.matcher(dateStr);
        return matcher.matches();
    }
}
