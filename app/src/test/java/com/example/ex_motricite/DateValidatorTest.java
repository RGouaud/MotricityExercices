package com.example.ex_motricite;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DateValidatorTest {

    // tests paramétrés
    @ParameterizedTest
    @CsvSource
            ({
                    "01/01/2020, true", // nominal
                    "31/01/2020, true", // max day in a month
                    "30/03/2020, true", // nominal
                    "01/12/2020, true", // max month in a year
                    "29/02/2020, true", // february has 29 days in 2020
                    "28/02/2021, true", // february has 28 days in 2021


                    "30/02/2020, false", // february has 29 days in 2020
                    "29/02/2021, false", // february has 28 days in 2021
                    "01/01/20, false", // year < 4 digits
                    "01/13/2020, false", // month > 12
                    "32/01/2020, false", // day > 31
                    "01/01/20200, false", // year > 4 digits
            })
    void isDateValid(String date, boolean expected) {
            assertEquals(expected, DateValidator.isValid(date));
    }
}