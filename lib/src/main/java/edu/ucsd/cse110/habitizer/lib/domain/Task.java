package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;

public class Task {
    private final @NonNull String taskName;
    private boolean checkedOff;

    public Task(@NonNull String taskName){
        this.taskName = taskName;
        this.checkedOff = false;
    }

    public String getTaskName(){
        return taskName;
    }

    public boolean getCheckedOffStatus() {
        return checkedOff;
    }

    public Task setName(String taskName){
        return new Task(taskName);
    }

    // Does not support unchecking
    public void setCheckedOff(){
        this.checkedOff = true;
    }
}
