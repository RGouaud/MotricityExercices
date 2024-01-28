package com.example.ex_motricite;

import static org.junit.Assert.*;
import org.junit.Test;

public class PatientTest {

    // CONSTRUCTORS
    @Test
    public void testPatientWhenAllParamsGiven() {
        //GIVEN
        long id = 1;
        String name = "Doe";
        String firstName = "John";
        String birthDate = "01/01/2000";
        String remarks = "No remarks";
        //WHEN
        Patient patient = new Patient(id, name, firstName, birthDate, remarks);
        // THEN
        assertEquals("Id of Patient : " + id + ", name of patient : " + name + ", firstname of patient : " + firstName + ", birthdate of patient : " + birthDate + ", remarks : " + remarks, patient.toString());
    }

    @Test
    public void testPatientWhenNoIdGiven() {
        //GIVEN
        String name = "Doe";
        String firstName = "John";
        String birthDate = "01/01/2000";
        String remarks = "No remarks";
        //WHEN
        Patient patient = new Patient(name, firstName, birthDate, remarks);
        // THEN
        assertEquals("Id of Patient : -1, name of patient : " + name + ", firstname of patient : " + firstName + ", birthdate of patient : " + birthDate + ", remarks : " + remarks, patient.toString());
    }
}