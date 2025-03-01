package edu.ucsd.cse110.habitizer.lib.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.habitizer.lib.domain.OriginalTask;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineBuilder;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineTimer;
import edu.ucsd.cse110.habitizer.lib.domain.SimpleTask;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.TaskList;
import edu.ucsd.cse110.habitizer.lib.util.Subject;
public class InMemoryDataSource {

    private final Map<Integer, Routine> routines
            = new HashMap<>();

    private final Map<Integer, Subject<Routine>> routineSubjects
            = new HashMap<>();

    private final Subject<List<Routine>> allRoutinesSubject
            = new Subject<>();


    private int nextId = 0;

    /*
    private int nextTaskId = 0;

    private final Map<Integer, Task> tasks
            = new HashMap<>();
    private final Map<Integer, Subject<Task>> taskSubjects
            = new HashMap<>();
    private final Subject<List<Task>> allTasksSubject
            = new Subject<>();

     */


    public InMemoryDataSource() {

    }

    public final static List<Task> Morning = List.of(
            new OriginalTask(0, "Morning Task 1",true),
            new OriginalTask(1, "Morning Task 2",true),
            new OriginalTask(2, "Morning Task 3",true),
            new OriginalTask(3, "Morning Task 4",true),
            new OriginalTask(4, "Morning Task 5",true)
    );

    public final static List<Task> Evening = List.of(
            new OriginalTask(0, "Evening Task 1",false),
            new OriginalTask(1, "Evening Task 2",false),
            new OriginalTask(2, "Evening Task 3",false),
            new OriginalTask(3, "Evening Task 4",false)
    );

    public final static List<SimpleTask> defaultTasks = List.of(

            new SimpleTask(0, "Morning Task 1",0),
            new SimpleTask(1, "Morning Task 2",0),
            new SimpleTask(2, "Morning Task 3",0),
            new SimpleTask(null, "Morning Task 4",0),
            new SimpleTask(null, "Morning Task 5",0),
            new SimpleTask(null, "Evening Task 1",1),
            new SimpleTask(null, "Evening Task 2",1),
            new SimpleTask(null, "Evening Task 3",1),
            new SimpleTask(null, "Evening Task 4",1),
            new SimpleTask(null, "Evening Task 5",1)
    );

    public final static List<Routine> DEFAULT = List.of(
            new RoutineBuilder()
                    .setId(null)
                    .setName("Morning")
                    //.setHasStarted(null)
                    //.setElapsedMinutes(0)
                    //.setElapsedSeconds(0)
                    .setTasks(Morning)
                    .setGoalTime("35")
                    .setTimer(new RoutineTimer(60))
                    .buildRoutine(),

            new RoutineBuilder()
                    .setId(null)
                    .setName("Evening")
                    //.setHasStarted(null)
                    //.setElapsedMinutes(0)
                    //.setElapsedSeconds(0)
                    .setTasks(Evening)
                    .setGoalTime("30")
                    .setTimer(new RoutineTimer(60))
                    .buildRoutine()
    );

    public static InMemoryDataSource DEFAULT(){
        var data = new InMemoryDataSource();
        for(Routine routine : DEFAULT){
            data.putRoutine(routine);
        }
        return data;
    }


    public void putRoutine(Routine routine) {
        var fixedRoutine = preInsertRoutine(routine);
        routines.put(fixedRoutine.getId(), fixedRoutine);
        if (routineSubjects.containsKey(fixedRoutine.getId())) {
            routineSubjects.get(fixedRoutine.getId()).setValue(fixedRoutine);
        }
        allRoutinesSubject.setValue(new ArrayList<Routine>(getRoutines()));
    }

    private Routine preInsertRoutine(Routine routine) {
        var id = routine.getId();
        if (id == null) {
            routine = routine.withId(nextId++);
            //nextId += 1;
        } else if (id >= nextId) {
            nextId = id + 1;
        }
        return routine;
    }

    public void removeRoutine(int id) {
        routines.remove(id);
        if (routineSubjects.containsKey(id)) {
            routineSubjects.get(id).setValue(null);
        }
        allRoutinesSubject.setValue(getRoutines());
    }

    public List<Routine> getRoutines(){
        return List.copyOf(routines.values());
    }

    public Routine getRoutine(int id){
        return routines.get(id);
    }
    public Subject<Routine> getRoutineSubject(int id){
        if(!routineSubjects.containsKey(id)){
            var subject = new Subject<Routine>();
            subject.setValue(getRoutine(id));
            routineSubjects.put(id, subject);
        }
        return routineSubjects.get(id);
    }

    public Subject<List<Routine>> getAllRoutinesSubject() {
        return allRoutinesSubject;
    }



    /*

    public void putTask(Task task) {
        var fixedCard = preInsertTask(task);
        tasks.put(fixedCard.getId(), fixedCard);
        if (taskSubjects.containsKey(fixedCard.getId())) {
            taskSubjects.get(fixedCard.getId()).setValue(fixedCard);
        }
        allTasksSubject.setValue(new ArrayList<Task>(getTasks()));
    }
    private Task preInsertTask(Task task) {
        var id = task.getId();
        if (id == null) {

            //nextTaskId += 1;
            task = task.withId(nextTaskId++);
        } else if (id >= nextTaskId) {
            nextTaskId = id + 1;
        }
        return task;
    }
    public void removeTask(int id) {
        tasks.remove(id);
        if (taskSubjects.containsKey(id)) {
            taskSubjects.get(id).setValue(null);
        }
        allTasksSubject.setValue(getTasks());
    }

    /*
    public void editTask(int id, String name){
        tasks.get(id).setName(name);
    }



    public List<Task> getTasks(){
        return List.copyOf(tasks.values());
    }

    public List<Task> getTasksWithRoutineId(int routineId){
        return tasks.values().stream()
                .filter(task -> task.getRoutineId() == routineId) // Filter tasks by routineID
                .collect(Collectors.toList());
    }

    public Task getTask(int id){
        return tasks.get(id);
    }


    public Subject<Task> getTaskSubject(int id){
        if(!taskSubjects.containsKey(id)){
            var subject = new Subject<Task>();
            subject.setValue(getTask(id));
            taskSubjects.put(id, subject);
        }
        return taskSubjects.get(id);
    }

    public Subject<List<Task>> getAllTasksSubject() {
        return allTasksSubject;
    }

     */


}


