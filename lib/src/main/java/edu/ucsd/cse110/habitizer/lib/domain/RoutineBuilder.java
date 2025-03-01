package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.ArrayList;
import java.util.List;
public class RoutineBuilder {

    private Integer id;
    private String name;
    private Boolean hasStarted;
    private Boolean hasEnded;
    private Integer elapsedMinutes;
    private Integer elapsedSeconds;
    private String goalTime;
    private List<Task> tasks;
    private RoutineTimer timer;

    public RoutineBuilder(){

    }

    public Routine buildRoutine() {
        return new Routine(this.id,this.name,this.hasStarted,this.hasEnded, this.elapsedMinutes,this.elapsedSeconds, this.goalTime, this.tasks,this.timer);
    }

    public RoutineBuilder setElapsedMinutes(Integer elapsedMinutes) {
        this.elapsedMinutes = elapsedMinutes;
        return this;
    }
    public RoutineBuilder setElapsedSeconds(Integer elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
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

    public RoutineBuilder setGoalTime(String time) {
        this.goalTime = time;
        return this;
    }
    public RoutineBuilder setTimer(RoutineTimer timer) {
        this.timer = timer;
        return this;
    }
}