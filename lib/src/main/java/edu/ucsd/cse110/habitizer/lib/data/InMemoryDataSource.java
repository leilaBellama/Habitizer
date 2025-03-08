package edu.ucsd.cse110.habitizer.lib.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import edu.ucsd.cse110.habitizer.lib.domain.OriginalTask;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineBuilder;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.util.Subject;
public class InMemoryDataSource {

    private final Map<Integer, Routine> routines
            = new HashMap<>();

    private final Map<Integer, Subject<Routine>> routineSubjects
            = new HashMap<>();

    private final Subject<List<Routine>> allRoutinesSubject
            = new Subject<>();

    private int nextId = 0;

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


    public final static List<Routine> DEFAULT = List.of(
            new RoutineBuilder()
                    .setId(null)
                    .setName("Morning")
                    .setTasks(Morning)
                    .setGoalTime("35")
                    .buildRoutine(),

            new RoutineBuilder()
                    .setId(null)
                    .setName("Evening")
                    .setTasks(Evening)
                    .setGoalTime("30")
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

}


