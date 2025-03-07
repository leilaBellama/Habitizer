package edu.ucsd.cse110.habitizer.lib.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import edu.ucsd.cse110.habitizer.lib.domain.OriginalTask;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineBuilder;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.util.MutableSubject;
import edu.ucsd.cse110.habitizer.lib.util.SimpleSubject;
import edu.ucsd.cse110.habitizer.lib.util.Subject;
public class InMemoryDataSource {

    private final Map<Integer, Routine> routines
            = new HashMap<>();

    private final Map<Integer, MutableSubject<Routine>> routineSubjects
            = new HashMap<>();

    private final MutableSubject<List<Routine>> allRoutinesSubject
            = new SimpleSubject<>();

    private int nextId = 0;

    private int nextTaskId = 0;

    private final Map<Integer, Task> tasks
            = new HashMap<>();
    private final Map<Integer, MutableSubject<Task>> taskSubjects
            = new HashMap<>();
    private final MutableSubject<List<Task>> allTasksSubject
            = new SimpleSubject<>();

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

    public final static List<Task> DEFAULT_TASKS = List.of(
            new OriginalTask(null, "Morning Task 1",true),
            new OriginalTask(null, "Morning Task 2",true),
            new OriginalTask(null, "Morning Task 3",true),
            new OriginalTask(null, "Evening Task 1",false),
            new OriginalTask(null, "Evening Task 2",false),
            new OriginalTask(null, "Evening Task 3",false),
            new OriginalTask(null, "Evening Task 4",false)
    );


    public final static List<Routine> DEFAULT_ROUTINES = List.of(
            new RoutineBuilder()
                    .setId(0)
                    .setName("Morning")
                    .setGoalTime("35")
                    .buildRoutine(),

            new RoutineBuilder()
                    .setId(1)
                    .setName("Evening")
                    .setGoalTime("30")
                    .buildRoutine()
    );

    public static InMemoryDataSource DEFAULT(){
        var data = new InMemoryDataSource();
        for(Routine routine : DEFAULT_ROUTINES){
            data.putRoutine(routine);
        }
        for(Task task : DEFAULT_TASKS){
            data.addTask(task);
        }
        return data;
    }
    public void addTask(Task task) {
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
            task.setId(nextTaskId++);
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

    public List<Task> getTasks(){
        return List.copyOf(tasks.values());
    }

    public Task getTask(int id){
        return tasks.get(id);
    }

    public Subject<Task> getTaskSubject(int id){
        if(!taskSubjects.containsKey(id)){
            var subject = new SimpleSubject<Task>();
            subject.setValue(getTask(id));
            taskSubjects.put(id, subject);
        }
        return taskSubjects.get(id);
    }

    public Subject<List<Task>> getAllTasksSubject() {
        return allTasksSubject;
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
            routine.setId(nextId++);
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
            var subject = new SimpleSubject<Routine>();
            subject.setValue(getRoutine(id));
            routineSubjects.put(id, subject);
        }
        return routineSubjects.get(id);
    }

    public Subject<List<Routine>> getAllRoutinesSubject() {
        return allRoutinesSubject;
    }

}


