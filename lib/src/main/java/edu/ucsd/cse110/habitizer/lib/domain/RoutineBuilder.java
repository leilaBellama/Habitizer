package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RoutineBuilder {

    private Integer id;
    private String name;
    private Boolean hasStarted;
    private Integer elapsedMinutes;
    private Integer elapsedSeconds;
    private String goalTime;

    public RoutineBuilder(){

    }

    /*
    public RoutineBuilder(@NonNull Routine routine){
        this.id = routine.id();
        this.elapsedMinutes = routine.elapsedMinutes();
        this.elapsedSeconds = routine.elapsedSeconds();
        this.name = routine.name();
        this.hasStarted = routine.hasStarted();
        this.goalTime = routine.goalTime();
    }

     */

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

    public RoutineBuilder makeNewRoutine(){
        this.setName("New Routine");
        return  this;
    }

    public Routine buildRoutine() {
        return new Routine(this.id,this.name,this.hasStarted, this.elapsedMinutes,this.elapsedSeconds, this.goalTime);
    }


}