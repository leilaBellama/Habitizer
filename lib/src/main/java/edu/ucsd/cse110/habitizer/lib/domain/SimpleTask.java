package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SimpleTask implements Task {
    //Added Id here, might not be useful now, but good for later when we need to insert our task
    private @Nullable Integer id;
    private @NonNull String taskName;
    private boolean checkedOff;
    private Integer checkedOffTime;
    private Integer routineId;



    public SimpleTask(@Nullable Integer id, @NonNull String taskName, Integer routineId){
        this.id = id;
        this.taskName = taskName;
        this.checkedOff = false;
        this.routineId = routineId;

        this.checkedOffTime = 0;
    }

    public SimpleTask(@Nullable Integer id, @NonNull String taskName){
        this.id = id;
        this.taskName = taskName;
        this.checkedOff = false;

        this.checkedOffTime = 0;
    }

    @Nullable
    @Override
    public Integer getId(){return id;}

    @Override
    public void setId(int id){this.id = id;}

    @Override
    public Integer getRoutineId(){return routineId;}

    @Override
    public void setRoutineId(int id){this.routineId = id;}

    @NonNull
    @Override
    public String getTaskName(){return taskName;}

    @Override
    public boolean getCheckedOffStatus() {return checkedOff;}


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
    public void reset() { this.checkedOff = false; this.checkedOffTime = 0;}

    //for testing purpose
    @Override
    public Task withId(int id){ return new SimpleTask(id, this.taskName,this.routineId); }


}