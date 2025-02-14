package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;

public class Task implements Serializable {
    //Added Id here, might not be useful now, but good for later when we need to insert our task
    private @Nullable Integer id;
    private @NonNull String taskName;
    private boolean checkedOff;
    private Integer checkedOffTime;

    private boolean isMorningTask;


    public Task(@Nullable Integer id, @NonNull String taskName, @NonNull Boolean isMorningTask){
        this.id = id;
        this.taskName = taskName;
        this.checkedOff = false;
        this.isMorningTask = isMorningTask;

        this.checkedOffTime = 0;
    }

    public @Nullable Integer getId(){return id;}

    public void setId(int id){this.id = id;}

    public @NonNull String getTaskName(){return taskName;}

    public boolean getCheckedOffStatus() {return checkedOff;}

    public boolean isMorningTask() {return isMorningTask;}

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
    public Task withId(int id){ return new Task(id, this.taskName, this.isMorningTask); }


}