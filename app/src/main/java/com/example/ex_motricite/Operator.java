package com.example.ex_motricite;

public class Operator {
    private long idOperator;
    private String name;
    private String firstName;

    public Operator(long idOperator, String name, String firstName){
        this.idOperator = idOperator;
        this.name = name;
        this.firstName = firstName;
    }

    public Operator( String name, String firstName){
        this.idOperator = -1;
        this.name = name;
        this.firstName = firstName;
    }

    public long getIdOperator(){
        return this.idOperator;
    }

    public String getName(){
        return this.name;
    }

    public String getFirstName(){
        return this.firstName;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String toString(){
        return "Id of Patient : " + this.getIdOperator() + ", name of patient : " + this.getName() + ", firstname of patient : " + this.getFirstName();
    }
}
