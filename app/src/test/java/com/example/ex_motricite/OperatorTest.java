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
}