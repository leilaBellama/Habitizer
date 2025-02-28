package edu.ucsd.cse110.habitizer.lib.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.util.Subject;
public class InMemoryDataSource {

    private int nextId = 0;

    private final Map<Integer, Task> tasks
            = new HashMap<>();
    private final Map<Integer, Subject<Task>> taskSubjects
            = new HashMap<>();
    private final Subject<List<Task>> allTasksSubject
            = new Subject<>();

    public InMemoryDataSource(){

    }

    public final static List<Task> DEFAULT = List.of(
            new Task(0, "Morning Task 1",true),
            new Task(1, "Morning Task 2",true),
            new Task(2, "Morning Task 3",true),
            new Task(3, "Evening Task 1",false),
            new Task(4, "Evening Task 2",false),
            new Task(5, "Evening Task 3",false),
            new Task(null, "Evening Task 4",false),
            new Task(null, "Evening Task 5",false)
    );

    public static InMemoryDataSource DEFAULT(){
        var data = new InMemoryDataSource();
        for(Task task : DEFAULT){
            data.putTask(task);
        }
        return data;
    }

    public void putTask(Task task) {
        var fixedCard = preInsert(task);
        tasks.put(fixedCard.getId(), fixedCard);
        if (taskSubjects.containsKey(fixedCard.getId())) {
            taskSubjects.get(fixedCard.getId()).setValue(fixedCard);
        }
        allTasksSubject.setValue(new ArrayList<Task>(getTasks()));
    }

    //if task to insert has null id, create new task with next id
    private Task preInsert(Task task) {
        var id = task.getId();
        if (id == null) {
            //nextId++;
            task = task.withId(nextId++);
        } else if (id > nextId) {
            nextId = id + 1;
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

    public void editTask(int id, String name){
        tasks.get(id).setName(name);
    }
    public List<Task> getTasks(){
        return List.copyOf(tasks.values());
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

}
/*
package edu.ucsd.cse110.habitizer.lib.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.util.Subject;
public class InMemoryDataSource {


    private final Map<Integer, Routine> routines
            = new HashMap<>();

    private final Map<Integer, Subject<Routine>> routineSubjects
            = new HashMap<>();

    private final Subject<List<Routine>> allRoutinesSubject
            = new Subject<>();

    private int nextRoutineId = 0;



    private int nextId = 0;

    private final Map<Integer, Task> tasks
            = new HashMap<>();
    private final Map<Integer, Subject<Task>> taskSubjects
            = new HashMap<>();
    private final Subject<List<Task>> allTasksSubject
            = new Subject<>();

    public InMemoryDataSource(){

    }

    public final static List<Task> Morning = List.of(
            new Task(0, "Morning Task 1",true),
            new Task(1, "Morning Task 2",true),
            new Task(2, "Morning Task 3",true),
            new Task(null, "Morning Task 4",true),
            new Task(null, "Morning Task 5",true)
    );

    public final static List<Task> Evening = List.of(
            new Task(0, "Evening Task 1",false),
            new Task(4, "Evening Task 2",false),
            new Task(5, "Evening Task 3",false),
            new Task(null, "Evening Task 4",false),
            new Task(null, "Evening Task 5",false)
    );
    public final static List<Task> DEFAULT = List.of(
            new Task(0, "Morning Task 1",true),
            new Task(1, "Morning Task 2",true),
            new Task(2, "Morning Task 3",true),
            new Task(3, "Evening Task 1",false),
            new Task(4, "Evening Task 2",false),
            new Task(5, "Evening Task 3",false),
            new Task(null, "Evening Task 4",false),
            new Task(null, "Evening Task 5",false)
    );

    public final static List<Routine> defaultRoutines = List.of(
            new Routine(false,"Evening Routine",null,null,null,false),

            new Routine(true,"Morning Routine",null,null,null,true)
    );

    public static InMemoryDataSource DEFAULT(){
        var data = new InMemoryDataSource();
        for(Routine routine : defaultRoutines){
            data.putRoutine(routine);
            //data.putRoutineSubject(routine);
        }
        for(Task task : DEFAULT){
            data.putTask(task);
        }
        return data;
    }

    public void putRoutine(Routine routine) {
        if(routine.getIsMorning()){
            routines.put(1,routine);
            var subject = new Subject<Routine>();
            subject.setValue(routine);
            routineSubjects.put(1, subject);
        } else {
            routines.put(0,routine);
            var subject = new Subject<Routine>();
            subject.setValue(routine);
            routineSubjects.put(0, subject);
        }
        //routines.put(nextRoutineId,routine);
        //nextRoutineId++;
        allRoutinesSubject.setValue(getRoutines());
    }

    public void putRoutineSubject(Routine routine) {
        int bool = routine.getIsMorning() ? 1 : 0;

        var subject = new Subject<Routine>();
        subject.setValue(routine);
        routineSubjects.put(bool, subject);
    }

    public List<Routine> getRoutines(){
        return List.copyOf(routines.values());
    }

    public Subject<Routine> getRoutineSubject(Boolean isMorning){
        if(isMorning == null) return null;
        if(isMorning){return routineSubjects.get(1);}
        else{return routineSubjects.get(0);}
    }

    public Routine getRoutine(Boolean isMorning){
        if(isMorning == null) return null;
        if(isMorning){return routines.get(0);}
        else{return routines.get(1);}
    }

    public Subject<Routine> getShowingRoutineSubject(){
        var morningSubject = routineSubjects.get(1);
        if(morningSubject.getValue().getIsShowing()) {
            return morningSubject;
        }
        return routineSubjects.get(0);
    }

    public Subject<Routine> getMorningRoutineSubject(){
        return routineSubjects.get(1);
    }

    public Subject<Routine> getEveningRoutineSubject(){
        return routineSubjects.get(0);
    }

    public Subject<List<Routine>> getAllRoutinesSubject() {
        return allRoutinesSubject;
    }

    public void removeRoutine(int id) {
        routines.remove(id);
        if (routineSubjects.containsKey(id)) {
            routineSubjects.get(id).setValue(null);
        }
        allRoutinesSubject.setValue(getRoutines());
    }

    //

    public void putTask(Task task) {
        var fixedCard = preInsert(task);
        tasks.put(fixedCard.getId(), fixedCard);
        if (taskSubjects.containsKey(fixedCard.getId())) {
            taskSubjects.get(fixedCard.getId()).setValue(fixedCard);
        }
        allTasksSubject.setValue(getTasks());
    }

    //if task to insert has null id, create new task with next id
    private Task preInsert(Task task) {
        var id = task.getId();
        if (id == null) {
            task = task.withId(nextId++);
        } else if (id > nextId) {
            nextId = id + 1;
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

    public List<Task> getTasks(){
        return List.copyOf(tasks.values());
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

}

 */
