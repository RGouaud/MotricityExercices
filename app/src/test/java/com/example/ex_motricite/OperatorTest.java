package com.example.ex_motricite;

import org.junit.Test;
import static org.junit.Assert.*;

public class OperatorTest {

    // CONSTRUCTORS
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

    @Test
    public void testToStringWhenNothingGiven(){
        //GIVEN
        //WHEN
        Operator operator = new Operator("","");
        //THEN
        assertEquals("Id of Patient : " + -1 + ", name of patient : " + "" + ", firstname of patient : " + "", operator.toString());
    }

    // GET NAME

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