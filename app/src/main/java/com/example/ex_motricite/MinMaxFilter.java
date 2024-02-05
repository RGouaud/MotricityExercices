package com.example.ex_motricite;

import android.text.InputFilter;
import android.text.Spanned;
// Custom class to define min and max for the edit text

/**
 * The {@code MinMaxFilter} class represents a custom input filter for edit text fields.
 * It allows users to set a minimum and maximum value for the edit text field.
 *
 * <p>
 * This class is used to set a minimum and maximum value for the edit text field.
 * </p>
 *
 * <p>
 *     Author: Segot
 *     Version: 1.0
 * </p>
 */
public class MinMaxFilter implements InputFilter {
    /**
     * The minimum value for the edit text field.
     */
    private final int intMin;
    /**
     * The maximum value for the edit text field.
     */
    private final int intMax;

    // Initialized

    /**
     * Constructor for MinMaxFilter class.
     *
     * @param minValue Minimum value for the edit text field.
     * @param maxValue Maximum value for the edit text field.
     */
    public MinMaxFilter(int minValue, int maxValue) {
        this.intMin = minValue;
        this.intMax = maxValue;
    }

    /**
     * Filters the input to ensure it is within the specified range.
     *
     * @param source The source text.
     * @param start The start index.
     * @param end The end index.
     * @param dest The destination text.
     * @param dStart The destination start index.
     * @param dEnd The destination end index.
     * @return The filtered text.
     */
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dStart, int dEnd) {
        try {
            String input = dest.toString() + source.toString();
            int inputValue = Integer.parseInt(input);
            if (isInRange(intMin, intMax, inputValue)) {
                return null;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Checks if the input value is within the specified range.
     *
     * @param a Minimum value.
     * @param b Maximum value.
     * @param c Input value.
     * @return True if the input value is within the specified range, false otherwise.
     */
    private boolean isInRange(int a, int b, int c) {
        if (b > a) {
            return c >= a && c <= b;
        } else {
            return c >= b && c <= a;
        }
    }
}
