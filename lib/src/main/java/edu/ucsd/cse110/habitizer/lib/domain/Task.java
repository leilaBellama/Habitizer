package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Task {
    //Added Id here, might not be useful now, but good for later when we need to insert our task
    private @Nullable Integer id;
    private @NonNull String taskName;
    private boolean checkedOff;


    public Task(@Nullable Integer id, @NonNull String taskName){
        this.id = id;
        this.taskName = taskName;
        this.checkedOff = false;
    }

    public Integer getId(){
        return id;
    }
    public String getTaskName(){
        return taskName;
    }

    public boolean getCheckedOffStatus() {
        return checkedOff;
    }

    public void setName(String taskName){
        this.taskName = taskName;
    }

    // Does not support unchecking
    public void setCheckedOff(boolean isChecked){
        this.checkedOff = isChecked;
    }

    //for testing purpose
    public Task withId(int id){
        return new Task(id, this.taskName);
    }


}
