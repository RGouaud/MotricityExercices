package com.example.ex_motricite;

public class Operator extends Actor {

    public Operator(long id, String name, String firstName){
        super(id, name, firstName);
    }

    public Operator( String name, String firstName){
        super(-1, name, firstName);
    }

    public String toString(){
        return "Id of Patient : " + this.getId() + ", name of patient : " + this.getName() + ", firstname of patient : " + this.getFirstName();
    }
}
