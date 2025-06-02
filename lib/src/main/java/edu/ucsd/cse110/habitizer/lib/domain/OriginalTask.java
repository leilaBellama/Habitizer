package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OriginalTask implements Task {
    private @Nullable Integer id;
    private @NonNull String taskName;
    private boolean checkedOff;
    private String checkedOffTime;
    private Integer routineId;

    private boolean isMorningTask;

    private Integer position;

    public OriginalTask(@Nullable Integer id, @NonNull String taskName, @NonNull Boolean isMorningTask, @NonNull Integer position){
        this.id = id;
        this.taskName = taskName;
        this.checkedOff = false;
        this.isMorningTask = isMorningTask;
        if(isMorningTask) this.routineId = 1;
        else this.routineId = 2;
        this.position = position;
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
    public void setCheckedOff(boolean isChecked, String checkedOffTime){
        if (!this.checkedOff) {
            this.checkedOff = isChecked;
            this.checkedOffTime = checkedOffTime;
        }
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
    public void setPosition(Integer position){ this.position = position;}

}