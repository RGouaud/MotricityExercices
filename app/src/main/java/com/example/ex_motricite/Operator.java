package com.example.ex_motricite;

import androidx.annotation.NonNull;

/**
 * The {@code Operator} class represents an operator.
 *
 * <p>
 * This class is used to represent an operator, with a unique ID, name, and first name.
 * </p>
 *
 * <p>
 *     Author: Ferreria
 *     Version: 1.0
 * </p>
 */
public class Operator extends Actor {

    /**
     * Constructor for Operator class.
     *
     * @param id Unique ID of the operator.
     * @param name Name of the operator.
     * @param firstName First name of the operator.
     */
    public Operator(long id, String name, String firstName){
        super(id, name, firstName);
    }

    /**
     * Constructor for Operator class.
     *
     * @param name Name of the operator.
     * @param firstName First name of the operator.
     */
    public Operator( String name, String firstName){
        super(-1, name, firstName);
    }

    /**
     * Returns a string representation of the operator.
     *
     * @return String representation of the operator.
     */
    @NonNull
    public String toString(){
        return "Id of Patient : " + this.getId() + ", name of patient : " + this.getName() + ", firstname of patient : " + this.getFirstName();
    }
}
