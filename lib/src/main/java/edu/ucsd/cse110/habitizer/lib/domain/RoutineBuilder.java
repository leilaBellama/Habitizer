package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.ArrayList;
import java.util.List;
public class RoutineBuilder {

    private Integer id;
    private String name;
    private Boolean hasStarted;
    private Boolean hasEnded;
    private Integer elapsedTime;
    private Integer goalTime;
    private List<Task> tasks;

    public RoutineBuilder(){

    }

    public Routine buildRoutine() {
        return new Routine(this.id,this.name,this.hasStarted,this.hasEnded, this.elapsedTime, this.goalTime, this.tasks);
    }

    public RoutineBuilder setElapsedTime(Integer elapsedTime) {
        this.elapsedTime = elapsedTime;
        return this;
    }

    public RoutineBuilder setHasStarted(Boolean hasStarted) {
        this.hasStarted = hasStarted;
        return this;
    }
    public RoutineBuilder setHasEnded(Boolean hasEnded) {
        this.hasEnded = hasEnded;
        return this;
    }

    public RoutineBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public RoutineBuilder setTasks(List<Task> tasks) {
        this.tasks = tasks;
        return this;
    }

    public RoutineBuilder setId(Integer id) {
        this.id = id;
        return this;
    }

    public RoutineBuilder setGoalTime(Integer time) {
        this.goalTime = time;
        return this;
    }
}