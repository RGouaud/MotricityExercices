package com.example.ex_motricite;

import android.text.InputFilter;
import android.text.Spanned;
// Custom class to define min and max for the edit text
public class MinMaxFilter implements InputFilter {
    private final int intMin;
    private final int intMax;

    // Initialized


    public MinMaxFilter(int minValue, int maxValue) {
        this.intMin = minValue;
        this.intMax = maxValue;
    }

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

    // Check if input c is in between min a and max b and
    // returns corresponding boolean
    private boolean isInRange(int a, int b, int c) {
        if (b > a) {
            return c >= a && c <= b;
        } else {
            return c >= b && c <= a;
        }
    }
}
