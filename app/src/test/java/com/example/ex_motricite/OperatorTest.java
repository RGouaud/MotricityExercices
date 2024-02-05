package com.example.ex_motricite;

import org.junit.Test;
import static org.junit.Assert.*;

public class OperatorTest {

    // CONSTRUCTORS

    /**
     * Test the constructor with name and firstName only
     */
    @Test
    public void testConstructor2Params(){
        //GIVEN
        String name = "Doe";
        String firstName = "John";
        //WHEN
        Operator operator = new Operator(name, firstName);
        //THEN
        assertEquals(operator.name, name);
        assertEquals(operator.firstName, firstName);
        assertEquals(-1, operator.id);
    }

    /**
     * Test the constructor with id, name and firstName
     */
    @Test
    public void testConstructor3Params(){
        //GIVEN
        long id = 1;
        String name = "Doe";
        String firstName = "John";
        //WHEN
        Operator operator = new Operator(id, name, firstName);
        //THEN
        assertEquals(operator.id, id);
        assertEquals(operator.name, name);
        assertEquals(operator.firstName, firstName);
    }

    // TO STRING

    /**
     * Test the toString method when all parameters are given
     */
    @Test
    public void testToStringWhenAllParamsGiven(){
        //GIVEN
        long id = 1;
        String name = "Doe";
        String firstName = "John";
        //WHEN
        Operator operator = new Operator(id, name, firstName);
        //THEN
        assertEquals("Id of Patient : " + id + ", name of patient : " + name + ", firstname of patient : " + firstName, operator.toString());
    }

    /**
     * Test the toString method when no id is given
     */
    @Test
    public void testToStringWhenNoIdGiven(){
        //GIVEN
        String name = "Doe";
        String firstName = "John";
        //WHEN
        Operator operator = new Operator(name, firstName);
        //THEN
        assertEquals("Id of Patient : " + -1 + ", name of patient : " + name + ", firstname of patient : " + firstName, operator.toString());
    }

    /**
     * Test the toString method when nothing is given
     */
    @Test
    public void testToStringWhenNothingGiven(){
        //GIVEN
        //WHEN
        Operator operator = new Operator("","");
        //THEN
        assertEquals("Id of Patient : " + -1 + ", name of patient : " + "" + ", firstname of patient : " + "", operator.toString());
    }

    // GET NAME
    /**
     * Test the getName method when name is given
     */
    @Test
    public void testGetNameWhenNameGiven(){
        //GIVEN
        String name = "Doe";
        String firstName = "John";
        //WHEN
        Operator operator = new Operator(name, firstName);
        //THEN
        assertEquals(name, operator.getName());
    }

    /**
     * Test the getName method when no name is given
     */
    @Test
    public void testGetNameWhenNoNameGiven(){
        //GIVEN
        String name = "";
        String firstName = "John";
        //WHEN
        Operator operator = new Operator(name, firstName);
        //THEN
        assertEquals(name, operator.getName());
    }

    // GET FIRST NAME
    /**
     * Test the getFirstName method when firstName is given
     */
    @Test
    public void testGetFirstNameWhenFirstNameGiven(){
        //GIVEN
        String name = "Doe";
        String firstName = "John";
        //WHEN
        Operator operator = new Operator(name, firstName);
        //THEN
        assertEquals(firstName, operator.getFirstName());
    }

    /**
     * Test the getFirstName method when no firstName is given
     */
    @Test
    public void testGetFirstNameWhenNoFirstNameGiven(){
        //GIVEN
        String name = "Doe";
        String firstName = "";
        //WHEN
        Operator operator = new Operator(name, firstName);
        //THEN
        assertEquals(firstName, operator.getFirstName());
    }

    // GET ID
    /**
     * Test the getId method when id is given
     */
    @Test
    public void testGetIdWhenIdGiven(){
        //GIVEN
        long id = 1;
        String name = "Doe";
        String firstName = "John";
        //WHEN
        Operator operator = new Operator(id, name, firstName);
        //THEN
        assertEquals(id, operator.getId());
    }

    /**
     * Test the getId method when no id is given
     */
    @Test
    public void testGetIdWhenNoIdGiven(){
        //GIVEN
        String name = "Doe";
        String firstName = "John";
        //WHEN
        Operator operator = new Operator(name, firstName);
        //THEN
        assertEquals(-1, operator.getId());
    }

    // SET NAME
    /**
     * Test the setName method when name is given
     */
    @Test
    public void testSetNameWhenNameGiven(){
        //GIVEN
        String name = "Doe";
        String firstName = "John";
        //WHEN
        Operator operator = new Operator(name, firstName);
        String newName = "Doe2";
        operator.setName(newName);
        //THEN
        assertEquals(newName, operator.getName());
    }

    /**
     * Test the setName method when no name is given
     */
    @Test
    public void testSetNameWhenNoNameGiven(){
        //GIVEN
        String name = "Doe";
        String firstName = "John";
        //WHEN
        Operator operator = new Operator(name, firstName);
        String newName = "";
        operator.setName(newName);
        //THEN
        assertEquals(newName, operator.getName());
    }

    // SET FIRST NAME
    /**
     * Test the setFirstName method when firstName is given
     */
    @Test
    public void testSetFirstNameWhenFirstNameGiven(){
        //GIVEN
        String name = "Doe";
        String firstName = "John";
        //WHEN
        Operator operator = new Operator(name, firstName);
        String newFirstName = "John2";
        operator.setFirstName(newFirstName);
        //THEN
        assertEquals(newFirstName, operator.getFirstName());
    }

    /**
     * Test the setFirstName method when no firstName is given
     */
    @Test
    public void testSetFirstNameWhenNoFirstNameGiven(){
        //GIVEN
        String name = "Doe";
        String firstName = "John";
        //WHEN
        Operator operator = new Operator(name, firstName);
        String newFirstName = "";
        operator.setFirstName(newFirstName);
        //THEN
        assertEquals(newFirstName, operator.getFirstName());
    }

}