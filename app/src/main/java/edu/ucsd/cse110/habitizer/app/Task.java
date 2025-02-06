package edu.ucsd.cse110.habitizer.app;

public class Task {
    private String name;
    private boolean checkedOff;

    public Task(String name){
        this.name = name;
        this.checkedOff = false;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    // Does not support unchecking
    public void checkedOff(){
        this.checkedOff = true;
    }
}
