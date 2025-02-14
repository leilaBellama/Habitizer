package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Task {
    //Added Id here, might not be useful now, but good for later when we need to insert our task
    private @Nullable Integer id;
    private @NonNull String taskName;
    private boolean checkedOff;
    private Integer checkedOffTime;


    public Task(@Nullable Integer id, @NonNull String taskName){
        this.id = id;
        this.taskName = taskName;
        this.checkedOff = false;
        this.checkedOffTime = 0;
    }

    public Integer getId(){
        return id;
    }
    public void setId(int id){this.id = id; }
    public String getTaskName(){
        return taskName;
    }

    public boolean getCheckedOffStatus() {
        return checkedOff;
    }

    public Integer getCheckedOffTime() { return checkedOffTime; }

    public void setName(String taskName){
        this.taskName = taskName;
    }

    // Does not support unchecking, adds another if statement
    // to prevent checkoff
    public void setCheckedOff(boolean isChecked, Integer checkedOffTime){
        if (!this.checkedOff) {
            this.checkedOff = isChecked;
            this.checkedOffTime = checkedOffTime;
        }
    }

    // Use this function for resetting after end routine
    public void reset() { this.checkedOff = false; }

    //for testing purpose
    public Task withId(int id){ return new Task(id, this.taskName); }


}