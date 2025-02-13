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
    /*
    private final Map<Integer, Task> tasksEvening
            = new HashMap<>();
    private final Map<Integer, Subject<Task>> taskSubjectsEvening
            = new HashMap<>();
    private final Subject<List<Task>> allTasksSubjectEvening
            = new Subject<>();

    private final Map<Integer, Task> tasksMorning
            = new HashMap<>();
    private final Map<Integer, Subject<Task>> taskSubjectsMorning
            = new HashMap<>();
    private final Subject<List<Task>> allTasksSubjectMorning
            = new Subject<>();

     */

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

    public void removeTask(int id) {
        tasks.remove(id);
        if (taskSubjects.containsKey(id)) {
            taskSubjects.get(id).setValue(null);
        }
        allTasksSubject.setValue(getTasks());
    }


       /**
     * Private utility method to maintain state of the fake DB: ensures that new
     * cards inserted have an id, and updates the nextId if necessary.
     */
    private Task preInsert(Task task) {
        var id = task.getId();
        if (id == null) {
            //nextId++;
            task = task.withId(nextId++);
        } else if (id > nextId) {
            nextId = id + 1;
        }
        /*
        if (task.isMorningTask()) {
            if (id == null) {
                task = task.withId(nextIdMorning++);
            } else {
                nextIdMorning = id++;
            }
        } else {
            if (id == null) {
                task = task.withId(nextIdEvening++);
            } else {
                nextIdEvening = id++;
            }
        }

         */
        return task;
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

    /*
    public void putTask(Task task){
        tasks.put(task.getId(), task);
        if(taskSubjects.containsKey(task.getId())){
            taskSubjects.get(task.getId()).setValue(task);
        }
        allTasksSubject.setValue(getTasks());
    }


    //evening methods

    public List<Task> getTasksEvening(){
        return List.copyOf(tasksEvening.values());
    }

    public Task getTaskEvening(int id){
        return tasksEvening.get(id);
    }

    public Subject<Task> getTaskSubjectEvening(int id){
        if(!taskSubjectsEvening.containsKey(id)){
            var subject = new Subject<Task>();
            subject.setValue(getTaskEvening(id));
            taskSubjectsEvening.put(id, subject);
        }
        return taskSubjectsEvening.get(id);
    }

    public Subject<List<Task>> getAllTasksSubjectEvening() {
        return allTasksSubjectEvening;
    }

    public void putTaskEvening(Task task){
        tasksEvening.put(task.getId(), task);
        if(taskSubjectsEvening.containsKey(task.getId())){
            taskSubjectsEvening.get(task.getId()).setValue(task);
        }
        allTasksSubjectEvening.setValue(getTasksEvening());
    }

     */

}
