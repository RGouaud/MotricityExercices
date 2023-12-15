package com.example.ex_motricite;

public class Patient {
    private long idPatient;
    private String name;
    private String firstName;
    private String birthDate;
    private String remarks;

    public Patient(long idPatient, String name, String firstName, String birthDate, String remarks){
        this.idPatient = idPatient;
        this.name = name;
        this.firstName = firstName;
        this.birthDate = birthDate;
        this.remarks = remarks;
    }

    public Patient( String name, String firstName, String birthDate, String remarks){
        this.idPatient = -1;
        this.name = name;
        this.firstName = firstName;
        this.birthDate = birthDate;
        this.remarks = remarks;
    }

    public long getIdPatient(){
        return this.idPatient;
    }

    public String getName(){
        return this.name;
    }

    public String getFirstName(){
        return this.firstName;
    }

    public String getBirthDate(){
        return this.birthDate;
    }

    public String getRemarks(){
        return this.remarks;
    }

    public void setIdPatient(long idPatient){
         this.idPatient = idPatient;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public void setBirthDate(String birthDate){
        this.birthDate = birthDate;
    }

    public void setRemarks(String remarks){
        this.remarks = remarks;
    }

    public String toString(){
        return "Id of Patient : " + this.getIdPatient() + ", name of patient : " + this.getName() + ", firstname of patient : " + this.getFirstName() + ", birthdate of patient : " + this.getBirthDate() + ", remarks : " + this.getRemarks();
    }
}
