package com.example.ex_motricite;

import static org.junit.Assert.*;
import org.junit.Test;

public class PatientTest {

    // CONSTRUCTORS
    /**
     * Test the constructor with all parameters
     */
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

    /**
     * Test the constructor with name, firstName and birthDate only
     */
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

    /**
     * Test the constructor with all parameters
     * Incorrect birthdate should throw an IllegalArgumentException
     */
    @Test
    public void testPatientWhenDateIsNotCorrectWithID(){
        // GIVEN
        long id = 1;
        String name = "Doe";
        String firstName = "John";
        String birthDate = "01/011/200";
        String remarks = "No remarks";
        // WHEN
        try{
            new Patient(id, name, firstName, birthDate, remarks);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e){

        // THEN
            assertEquals("Birthdate is not valid", e.getMessage());
        }
    }

    /**
     * Test the constructor with name, firstName and birthDate only
     * Incorrect birthdate should throw an IllegalArgumentException
     */
    @Test
    public void testPatientWhenDateIsNotCorrectWithoutID(){
        // GIVEN
        String name = "Doe";
        String firstName = "John";
        String birthDate = "01/011/200";
        String remarks = "No remarks";
        // WHEN
        try{
            new Patient(name, firstName, birthDate, remarks);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e){

            // THEN
            assertEquals("Birthdate is not valid", e.getMessage());
        }
    }

    // GETTERS
    /**
     * Test the getBirthDate method
     */
    @Test
    public void testGetBirthDate(){
        // GIVEN
        long id = 1;
        String name = "Doe";
        String firstName = "John";
        String birthDate = "01/01/2000";
        String remarks = "No remarks";
        Patient patient = new Patient(id, name, firstName, birthDate, remarks);
        // WHEN
        String birthDatePatient = patient.getBirthDate();
        // THEN
        assertEquals(birthDate, birthDatePatient);
    }

    /**
     * Test the getRemarks method
     */
    @Test
    public void testGetRemarks(){
        // GIVEN
        long id = 1;
        String name = "Doe";
        String firstName = "John";
        String birthDate = "01/01/2000";
        String remarks = "No remarks";
        Patient patient = new Patient(id, name, firstName, birthDate, remarks);
        // WHEN
        String remarksPatient = patient.getRemarks();
        // THEN
        assertEquals(remarks, remarksPatient);
    }

    /**
     * Test the getRemarks method when id is empty
     */
    @Test
    public void testGetRemarksWhenEmpty(){
        // GIVEN
        long id = 1;
        String name = "Doe";
        String firstName = "John";
        String birthDate = "01/01/2000";
        String remarks = "";
        Patient patient = new Patient(id, name, firstName, birthDate, remarks);
        // WHEN
        String remarksPatient = patient.getRemarks();
        // THEN
        assertEquals(remarks, remarksPatient);
    }

    /**
     * Test the getName method
     */
    @Test
    public void testGetName(){
        // GIVEN
        long id = 1;
        String name = "Doe";
        String firstName = "John";
        String birthDate = "01/01/2000";
        String remarks = "No remarks";
        Patient patient = new Patient(id, name, firstName, birthDate, remarks);
        // WHEN
        String namePatient = patient.getName();
        // THEN
        assertEquals(name, namePatient);
    }

    /**
     * Test the getFirstName method
     */
    @Test
    public void testGetFirstName(){
        // GIVEN
        long id = 1;
        String name = "Doe";
        String firstName = "John";
        String birthDate = "01/01/2000";
        String remarks = "No remarks";
        Patient patient = new Patient(id, name, firstName, birthDate, remarks);
        // WHEN
        String firstNamePatient = patient.getFirstName();
        // THEN
        assertEquals(firstName, firstNamePatient);
    }

    // SETTERS
    /**
     * Test the setBirthDate method
     */
    @Test
    public void testSetBirthDate(){
        // GIVEN
        long id = 1;
        String name = "Doe";
        String firstName = "John";
        String birthDate = "01/01/2000";
        String birthDate2 = "02/02/2002";
        String remarks = "No remarks";
        Patient patient = new Patient(id, name, firstName, birthDate, remarks);
        // WHEN
        patient.setBirthDate(birthDate2);
        // THEN
        assertEquals(birthDate2, patient.getBirthDate());
    }

    /**
     * Test the setBirthdate method
     * Incorrect birthdate should throw an IllegalArgumentException
     */
    @Test
    public void testSetWrongBirthDate(){
        // GIVEN
        long id = 1;
        String name = "Doe";
        String firstName = "John";
        String birthDate = "01/01/2000";
        String birthDate2 = "02/02/200";
        String remarks = "No remarks";
        Patient patient = new Patient(id, name, firstName, birthDate, remarks);
        // WHEN
        try{
            patient.setBirthDate(birthDate2);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e){

            // THEN
            assertEquals("Birthdate is not valid", e.getMessage());
        }
    }

    /**
     * Test the setRemarks method
     */
    @Test
    public void testSetRemarks(){
        // GIVEN
        long id = 1;
        String name = "Doe";
        String firstName = "John";
        String birthDate = "01/01/2000";
        String remarks = "No remarks";
        String remarks2 = "Remarks";
        Patient patient = new Patient(id, name, firstName, birthDate, remarks);
        // WHEN
        patient.setRemarks(remarks2);
        // THEN
        assertEquals(remarks2, patient.getRemarks());
    }

    /**
     * Test the setRemarks method with blank remarks
     */
    @Test
    public void testSetEmptyRemarks(){
        // GIVEN
        long id = 1;
        String name = "Doe";
        String firstName = "John";
        String birthDate = "01/01/2000";
        Patient patient = new Patient(id, name, firstName, birthDate, "");
        // WHEN
        String remarksPatient = patient.getRemarks();
        // THEN
        assertEquals("", remarksPatient);
    }

    /**
     * Test the setName method
     */
    @Test
    public void testSetName(){
        // GIVEN
        long id = 1;
        String name = "Doe";
        String name2 = "Doe2";
        String firstName = "John";
        String birthDate = "01/01/2000";
        String remarks = "No remarks";
        Patient patient = new Patient(id, name, firstName, birthDate, remarks);
        // WHEN
        patient.setName(name2);
        // THEN
        assertEquals(name2, patient.getName());
    }

    /**
     * Test the setFirstName method
     */
    @Test
    public void testSetFirstName(){
        // GIVEN
        long id = 1;
        String name = "Doe";
        String firstName = "John";
        String firstName2 = "John2";
        String birthDate = "01/01/2000";
        String remarks = "No remarks";
        Patient patient = new Patient(id, name, firstName, birthDate, remarks);
        // WHEN
        patient.setFirstName(firstName2);
        // THEN
        assertEquals(firstName2, patient.getFirstName());
    }
}