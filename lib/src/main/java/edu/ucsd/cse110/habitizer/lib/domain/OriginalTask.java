package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OriginalTask implements Task {
    //Added Id here, might not be useful now, but good for later when we need to insert our task
    private @Nullable Integer id;
    private @NonNull String taskName;
    private boolean checkedOff;
    private Integer checkedOffTime;

    private boolean isMorningTask;


    public OriginalTask(@Nullable Integer id, @NonNull String taskName, @NonNull Boolean isMorningTask){
        this.id = id;
        this.taskName = taskName;
        this.checkedOff = false;
        this.isMorningTask = isMorningTask;

        this.checkedOffTime = 0;
    }

    @Nullable
    @Override
    public Integer getId(){return id;}

    @Override
    public void setId(int id){this.id = id;}

    @NonNull
    @Override
    public String getTaskName(){return taskName;}

    @Override
    public boolean getCheckedOffStatus() {return checkedOff;}

    public boolean isMorningTask() {return isMorningTask;}

    public void setMorningTask(boolean isMorningTask){
        this.isMorningTask = isMorningTask;
    }

    @Override
    public Integer getCheckedOffTime() { return checkedOffTime; }

    @Override
    public void setName(String taskName){
        this.taskName = taskName;
    }

    // Does not support unchecking, adds another if statement
    // to prevent checkoff
    @Override
    public void setCheckedOff(boolean isChecked, Integer checkedOffTime){
        if (!this.checkedOff) {
            this.checkedOff = isChecked;
            this.checkedOffTime = checkedOffTime;
        }
    }

    // Use this function for resetting after end routine
    @Override
    public void reset() { this.checkedOff = false; }

    //for testing purpose
    @Override
    public Task withId(int id){ return new OriginalTask(id, this.taskName, this.isMorningTask); }

    @Override
    public int getRoutineId(){
        return -1;
    }
}