package com.example.ex_motricite;

public class Patient extends Actor {
    private String birthDate;
    private String remarks;

    public Patient(long id, String name, String firstName, String birthDate, String remarks){
        super(id, name, firstName);
        this.birthDate = birthDate;
        this.remarks = remarks;
    }

    public Patient( String name, String firstName, String birthDate, String remarks){
        super(-1, name, firstName);
        this.birthDate = birthDate;
        this.remarks = remarks;
    }

    public long getId(){
        return this.id;
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
         this.id = idPatient;
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
        return "Id of Patient : " + this.getId() + ", name of patient : " + this.getName() + ", firstname of patient : " + this.getFirstName() + ", birthdate of patient : " + this.getBirthDate() + ", remarks : " + this.getRemarks();
    }
}
