package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.ArrayList;
import java.util.List;
public class RoutineBuilder {

    private Integer id;
    private String name;
    private Boolean hasStarted;
    private Integer elapsedTime;
    private List<Task> tasks;

    public RoutineBuilder(){

    }

    public Routine buildRoutine() {
        return new Routine(this.id,this.name,this.hasStarted, this.elapsedTime, this.tasks);
    }

    public RoutineBuilder setElapsedTime(Integer elapsedTime) {
        this.elapsedTime = elapsedTime;
        return this;
    }

    public RoutineBuilder setHasStarted(Boolean hasStarted) {
        this.hasStarted = hasStarted;
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
}