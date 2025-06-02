package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * Buuilder class for Routine objects
 */
public class RoutineBuilder {

    private Integer id;
    private String name;
    private Boolean hasStarted;
    private Integer elapsedMinutes;
    private Integer elapsedSeconds;
    private Integer currentTaskTime;
    private String goalTime;

    private List<Task> tasks;
    public RoutineBuilder(){

    }

    public RoutineBuilder setElapsedMinutes(@Nullable Integer elapsedMinutes) {
        this.elapsedMinutes = elapsedMinutes;
        return this;
    }
    public RoutineBuilder setElapsedSeconds(@Nullable Integer elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
        return this;
    }

    public RoutineBuilder setHasStarted(@Nullable Boolean hasStarted) {
        this.hasStarted = hasStarted;
        return this;
    }

    public RoutineBuilder setName(@Nullable String name) {
        this.name = name;
        return this;
    }

    public RoutineBuilder setId(@Nullable Integer id) {
        this.id = id;
        return this;
    }

    public RoutineBuilder setGoalTime(@Nullable String time) {
        this.goalTime = time;
        return this;
    }

    public RoutineBuilder setCurrentTaskTime(@Nullable Integer time) {
        this.currentTaskTime = time;
        return this;
    }

    public RoutineBuilder setTasks(@Nullable List<Task> tasks){
        this.tasks = tasks;
        return this;
    }
    public RoutineBuilder makeNewRoutine(){
        this.setName("New Routine");
        return  this;
    }

    public Routine buildRoutine() {
        return new Routine(this.id,this.name,this.hasStarted, this.elapsedMinutes,this.elapsedSeconds, this.goalTime, this.currentTaskTime, this.tasks);
    }


}