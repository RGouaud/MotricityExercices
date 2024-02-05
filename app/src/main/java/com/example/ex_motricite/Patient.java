package com.example.ex_motricite;

import androidx.annotation.NonNull;

public class Patient extends Actor {
    private String birthDate;
    private String remarks;

    private static final String BIRTH_DATE_ERROR = "Birthdate is not valid";

    public Patient(long id, String name, String firstName, String birthDate, String remarks){
        super(id, name, firstName);
        if(!DateValidator.isValid(birthDate)){
            throw new IllegalArgumentException(BIRTH_DATE_ERROR);
        }
        this.birthDate = birthDate;
        this.remarks = remarks;
    }

    public Patient( String name, String firstName, String birthDate, String remarks){
        super(-1, name, firstName);
        if(!DateValidator.isValid(birthDate)){
            throw new IllegalArgumentException(BIRTH_DATE_ERROR);
        }
        this.birthDate = birthDate;
        this.remarks = remarks;
    }
    public String getBirthDate(){
        return this.birthDate;
    }

    public String getRemarks(){
        return this.remarks;
    }


    public void setBirthDate(String birthDate){
        if(!DateValidator.isValid(birthDate)){
            throw new IllegalArgumentException(BIRTH_DATE_ERROR);
        }
        this.birthDate = birthDate;
    }

    public void setRemarks(String remarks){
        this.remarks = remarks;
    }

    @NonNull
    public String toString(){
        return "Id of Patient : " + this.getId() + ", name of patient : " + this.getName() + ", firstname of patient : " + this.getFirstName() + ", birthdate of patient : " + this.getBirthDate() + ", remarks : " + this.getRemarks();
    }
}
