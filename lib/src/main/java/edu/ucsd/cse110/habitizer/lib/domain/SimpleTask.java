package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Original implementation of Task before refractoring
 */
public class SimpleTask implements Task {
    private @Nullable Integer id;
    private @NonNull String taskName;
    private boolean checkedOff;
    private String checkedOffTime;
    private Integer routineId;
    private Integer position;

    public SimpleTask(@Nullable Integer id, @NonNull String taskName,Boolean checkedOff, String checkedOffTime, Integer routineId, Integer position){
        this.id = id;
        this.taskName = taskName;
        this.checkedOff = checkedOff;
        this.checkedOffTime = checkedOffTime;
        this.routineId = routineId;
        this.position = position;
    }

    //for existing tests
    public SimpleTask(@Nullable Integer id, @NonNull String taskName){
        this.id = id;
        this.taskName = taskName;
        this.checkedOff = false;

        this.checkedOffTime = "0 secs";
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

    @Override
    public void setCheckedOff(boolean isChecked){
        this.checkedOff = isChecked;
    }

    @Override
    public String getCheckedOffTime() { return checkedOffTime; }

    @Override
    public void setCheckedOffTime(String time){
        this.checkedOffTime = time;
    }

    @Override
    public Integer getPosition() { return position;}

    @Override
    public void setPosition(Integer position) { this.position = position;}
}