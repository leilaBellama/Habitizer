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
