package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * Builder class for Task objects
 */
public class SimpleTaskBuilder {

    private @Nullable Integer id;
    private @NonNull String taskName;
    private boolean checkedOff;
    private String checkedOffTime;
    private Integer routineId;

    private Integer position;

    public SimpleTaskBuilder(){

    }

    public SimpleTask buildSimpleTask() {
        return new SimpleTask(this.id,this.taskName,this.checkedOff, this.checkedOffTime,this.routineId,this.position);
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
    public SimpleTaskBuilder setCheckedOffTime(String checkedOffTime) {
        this.checkedOffTime = checkedOffTime;
        return this;
    }

    public SimpleTaskBuilder setRoutineId(Integer id) {
        this.routineId = id;
        return this;
    }

    public SimpleTaskBuilder setPosition(Integer position){
        this.position = position;
        return this;
    }

}