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
}
