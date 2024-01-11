package com.example.ex_motricite;

public abstract class Actor {
    protected long id;
    protected String name;
    protected String firstName;

    protected Actor(long id, String name, String firstName){
        this.id = id;
        this.name = name;
        this.firstName = firstName;
    }

    public String getName(){
        return this.name;
    }

    public String getFirstName(){
        return this.firstName;
    }

    public long getId(){
        return this.id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public void setId(long id){
        this.id = id;
    }
}
