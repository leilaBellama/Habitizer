package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SimpleTaskBuilder {

    private @Nullable Integer id;
    private @NonNull String taskName;
    private boolean checkedOff;
    private Integer checkedOffTime;
    private Integer routineId;

    public SimpleTaskBuilder(){

    }

    public SimpleTaskBuilder(Task task){
        this.id = task.getId();
        this.taskName = task.getName();
        this.checkedOff = task.getCheckedOffStatus();
        this.checkedOffTime = task.getCheckedOffTime();
        this.routineId = task.getRoutineId();
    }

    public Task buildSimpleTask() {
        return new SimpleTask(this.id,this.taskName,this.checkedOff, this.checkedOffTime,this.routineId);
    }

    public SimpleTaskBuilder setId(Integer id) {
        this.id = id;
        return this;
    }

    public SimpleTaskBuilder setTaskName(String taskName) {
        this.taskName = taskName;
        return this;
    }

    public SimpleTaskBuilder setCheckedOff(Boolean checkedOff) {
        this.checkedOff = checkedOff;
        return this;
    }
    public SimpleTaskBuilder setCheckedOffTime(Integer checkedOffTime) {
        this.checkedOffTime = checkedOffTime;
        return this;
    }

    public SimpleTaskBuilder setRoutineId(Integer id) {
        this.routineId = id;
        return this;
    }

}