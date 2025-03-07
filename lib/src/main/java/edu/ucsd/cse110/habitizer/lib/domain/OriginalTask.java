package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OriginalTask implements Task {
    //Added Id here, might not be useful now, but good for later when we need to insert our task
    private @Nullable Integer id;
    private @NonNull String taskName;
    private boolean checkedOff;
    private Integer checkedOffTime;
    private Integer routineId;

    private boolean isMorningTask;


    public OriginalTask(@Nullable Integer id, @NonNull String taskName, @NonNull Boolean isMorningTask){
        this.id = id;
        this.taskName = taskName;
        this.checkedOff = false;
        this.isMorningTask = isMorningTask;
        if(isMorningTask) this.routineId = 0;
        else this.routineId = 1;

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
    public String getName(){return taskName;}

    @Override
    public void setName(@NonNull String taskName){
        this.taskName = taskName;
    }

    @Override
    public boolean getCheckedOffStatus() {return checkedOff;}

    public boolean isMorningTask() {return isMorningTask;}

    public void setMorningTask(boolean isMorningTask){
        this.isMorningTask = isMorningTask;
    }

    @Override
    public void setCheckedOff(boolean isChecked){
        this.checkedOff = isChecked;
    }

    // Does not support unchecking, adds another if statement
    // to prevent checkoff
    public void setCheckedOff(boolean isChecked, Integer checkedOffTime){
        if (!this.checkedOff) {
            this.checkedOff = isChecked;
            this.checkedOffTime = checkedOffTime;
        }
    }

    @Override
    public Integer getCheckedOffTime() { return checkedOffTime; }

    @Override
    public void setCheckedOffTime(Integer time){
        this.checkedOffTime = time;
    }

    /*
    // Use this function for resetting after end routine
    @Override
    public void reset() { this.checkedOff = false; this.checkedOffTime = 0;}

    //for testing purpose
    @Override
    public Task withId(int id){ return new OriginalTask(id, this.taskName, this.isMorningTask); }


     */
}