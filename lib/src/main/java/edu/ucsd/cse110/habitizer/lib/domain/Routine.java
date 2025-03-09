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
    private List<Task> tasks;
    //private RoutineTimer timer;


    public Routine(@Nullable Integer id, @Nullable String name, @Nullable Boolean hasStarted, @Nullable Integer elapsedMinutes,@Nullable Integer elapsedSeconds, @Nullable String goalTime,@Nullable List<Task> tasks){
        this.id = id;
        this.name = name;
        this.hasStarted = hasStarted;
        this.elapsedMinutes = elapsedMinutes;
        this.elapsedSeconds = elapsedSeconds;
        this.goalTime = goalTime;
        //this.timer = timer;
        this.tasks = (tasks != null) ? List.copyOf(tasks) : new ArrayList<>();
    }

    public static List<Routine> swapCurrentRoutine(List<Routine> list, Integer id){
        var copy = new ArrayList<>(List.copyOf(list));
        var current = copy.get(0).withId(id);
        var next = copy.get(id).withId(0);
        copy.set(0,next);
        copy.set(id,current);
        return copy;
    }

    public Routine reset(){
        return new Routine(this.getId(),this.getName(),false,0,0,this.getGoalTime(),new ArrayList<>());
    }

    public Routine(){
        this.name = "New Routine";
        this.goalTime = "---";
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

    public List<Task> getTasks() {return tasks;}


    public Routine withId(Integer id){
        return new Routine(id,this.name,this.hasStarted, this.elapsedMinutes,this.elapsedSeconds, this.goalTime, this.tasks);
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

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public void setGoalTime(String goalTime) {
        this.goalTime = goalTime;
    }

}