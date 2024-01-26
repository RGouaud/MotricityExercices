package com.example.ex_motricite;

import androidx.annotation.NonNull;

/**
 * The {@code Patient} class represents a patient.
 *
 * <p>
 * This class is used to represent a patient, with a unique ID, name, first name, birth date, and remarks.
 * </p>
 *
 * <p>
 *     Author: Ferreria
 *     Version: 1.0
 * </p>
 */
public class Patient extends Actor {
    private String birthDate;
    private String remarks;

    /**
     * Constructor for Patient class.
     *
     * @param id Unique ID of the patient.
     * @param name Name of the patient.
     * @param firstName First name of the patient.
     * @param birthDate Birth date of the patient.
     * @param remarks Remarks of the patient.
     */
    public Patient(long id, String name, String firstName, String birthDate, String remarks){
        super(id, name, firstName);
        this.birthDate = birthDate;
        this.remarks = remarks;
    }

    /**
     * Constructor for Patient class.
     *
     * @param name Name of the patient.
     * @param firstName First name of the patient.
     * @param birthDate Birth date of the patient.
     * @param remarks Remarks of the patient.
     */
    public Patient( String name, String firstName, String birthDate, String remarks){
        super(-1, name, firstName);
        this.birthDate = birthDate;
        this.remarks = remarks;
    }

    /**
     * Get the birth date of the patient.
     *
     * @return The birth date of the patient.
     */
    public String getBirthDate(){
        return this.birthDate;
    }

    /**
     * Get the remarks of the patient.
     *
     * @return The remarks of the patient.
     */
    public String getRemarks(){
        return this.remarks;
    }

    /**
     * Set the birth date of the patient.
     *
     * @param birthDate The birth date of the patient.
     */
    public void setBirthDate(String birthDate){
        this.birthDate = birthDate;
    }

    /**
     * Set the remarks of the patient.
     *
     * @param remarks The remarks of the patient.
     */
    public void setRemarks(String remarks){
        this.remarks = remarks;
    }

    /**
     * Returns a string representation of the patient.
     *
     * @return String representation of the patient.
     */
    @NonNull
    public String toString(){
        return "Id of Patient : " + this.getId() + ", name of patient : " + this.getName() + ", firstname of patient : " + this.getFirstName() + ", birthdate of patient : " + this.getBirthDate() + ", remarks : " + this.getRemarks();
    }
}
