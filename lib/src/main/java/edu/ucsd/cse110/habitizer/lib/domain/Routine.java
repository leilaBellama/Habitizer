package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Routine {

    private Integer id;
    private String name;
    private Boolean hasStarted;
    private Integer elapsedMinutes;
    private Integer elapsedSeconds;
    private String goalTime;

    public Routine(@Nullable Integer id, @Nullable String name, @Nullable Boolean hasStarted, @Nullable Integer elapsedMinutes, @Nullable Integer elapsedSeconds, @Nullable String goalTime) {
        this.id = id;
        this.name = name;
        this.hasStarted = hasStarted;
        this.elapsedMinutes = elapsedMinutes;
        this.elapsedSeconds = elapsedSeconds;
        this.goalTime = goalTime;
    }

    public void setElapsedMinutes(Integer elapsedMinutes) {
        this.elapsedMinutes = elapsedMinutes;
    }
    public void setElapsedSeconds(Integer elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
    }

    public void setHasStarted(Boolean hasStarted) {
        this.hasStarted = hasStarted;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public void setGoalTime(String goalTime) {
        this.goalTime = goalTime;
    }

    public Integer getId() {return id;}

    public String getName() {
        return name;
    }
    public Boolean getHasStarted() {
        return hasStarted;
    }

    public Integer getElapsedMinutes() {
        return elapsedMinutes;
    }
    public Integer getElapsedSeconds() {
        return elapsedSeconds;
    }
    public String getGoalTime() {
        return goalTime;
    }

}