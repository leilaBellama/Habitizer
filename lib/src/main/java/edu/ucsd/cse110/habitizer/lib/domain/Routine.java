package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
public class Routine {

    private Integer id;
    private String name;
    private Boolean hasStarted;
    private Boolean hasEnded;
    private Integer elapsedTime;
    private Integer goalTime;
    private List<Task> tasks;

    public Routine(@Nullable Integer id, @Nullable String name, @Nullable Boolean hasStarted, @Nullable Boolean hasEnded, @Nullable Integer elapsedTime, @Nullable Integer goalTime,@Nullable List<Task> tasks){
        this.id = id;
        this.name = name;
        this.hasStarted = hasStarted;
        this.hasEnded = hasEnded;
        this.elapsedTime = elapsedTime;
        this.goalTime = goalTime;
        this.tasks = (tasks != null) ? List.copyOf(tasks) : new ArrayList<>();
    }


    public Integer getId() {return id;}

    public String getName() {
        return name;
    }
    public Boolean getHasStarted() {
        return hasStarted;
    }

    public Integer getElapsedTime() {
        return elapsedTime;
    }
    public Integer getGoalTime() {
        return goalTime;
    }

    public List<Task> getTasks() {return tasks;}

    public Routine withId(Integer id){
        return new Routine(id,this.name,this.hasStarted,this.hasEnded, this.elapsedTime, this.goalTime, this.tasks);
    }

    public void setElapsedTime(Integer elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public void setHasStarted(Boolean hasStarted) {
        this.hasStarted = hasStarted;
    }
    public void setHasEnded(Boolean hasEnded) {
        this.hasEnded = hasEnded;
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

    /*
    public static List<Routine> swapIsShowing(List<Routine> routines){
        var newList = new ArrayList<Routine>();
        for(var routine: routines){
            newList.add(new Routine(routine.getIsMorning(), routine.getName(), routine.getHasStarted(), routine.getElapsedTime(), routine.getRoutineTasks(),!routine.getIsShowing()));
        }
        return newList;
    }

    public Routine startRoutine(){
        return new Routine(this.isMorning,this.name, true, 0,this.tasks,this.isShowing);
    }

    public static Routine setElapsedTime(Routine routine, Integer elapsedTime) {
        //this.elapsedTime = elapsedTime;
        return new Routine(routine.getIsMorning(),routine.getName(),routine.getHasStarted(), elapsedTime, routine.getRoutineTasks(),routine.getIsShowing());
    }

    public static Routine setHasStarted(Routine routine, Boolean hasStarted) {
        return new Routine(routine.getIsMorning(),routine.getName(),hasStarted, routine.getElapsedTime(), routine.getRoutineTasks(),routine.getIsShowing());
        //return new Routine(this.isMorning,this.name,hasStarted, this.elapsedTime, this.tasks);
    }


    public static Routine setName(Routine routine, String name) {
        return new Routine(routine.getIsMorning(),name,routine.getHasStarted(), routine.getElapsedTime(), routine.getRoutineTasks(),routine.getIsShowing());
        //return new Routine(this.isMorning,name,this.hasStarted, this.elapsedTime, this.tasks);
    }

    public static Routine setTasks(Routine routine, List<Task> tasks) {
        return new Routine(routine.getIsMorning(),routine.getName(),routine.getHasStarted(), routine.getElapsedTime(), tasks,routine.getIsShowing());

        //this.tasks = tasks;
        //return new Routine(this.isMorning,this.name,this.hasStarted, this.elapsedTime, tasks);
    }

    public Routine setIsMorning(Boolean isMorning) {
        //return new Routine(isMorning,routine.getName(),routine.getHasStarted(), routine.getElapsedTime(), routine.getRoutineTasks(),routine.getIsShowing());

        this.isMorning = isMorning;
        return this;
        //return new Routine(isMorning,this.name,this.hasStarted, this.elapsedTime, this.tasks);
    }
    public Routine swapShowing(Routine routine) {
        return new Routine(routine.getIsMorning(),routine.getName(),routine.getHasStarted(), routine.getElapsedTime(), routine.getRoutineTasks(),!routine.getIsShowing());

        //this.isShowing = isShowing;
        //return new Routine(isMorning,this.name,this.hasStarted, this.elapsedTime, this.tasks);
    }

     */
}