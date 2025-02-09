package edu.ucsd.cse110.habitizer.lib.data;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.util.Subject;
public class InMemoryDataSource {
    private final Map<Integer, Task> tasks
            = new HashMap<>();

    private final Map<Integer, Subject<Task>> taskSubjects
            = new HashMap<>();

    private final Subject<List<Task>> allTasksSubject
            = new Subject<>();

    public InMemoryDataSource(){

    }

    public final static List<Task> DEFAULT_TASKS = List.of(
            new Task(0, "Task 1"),
            new Task(1, "Task 2"),
            new Task(2, "Task 3")
    );

    public static InMemoryDataSource fromDefault(){
        var data = new InMemoryDataSource();
        for(Task task : DEFAULT_TASKS){
            data.putTask(task);
        }

        return data;
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

    public void putTask(Task task){
        tasks.put(task.getId(), task);
        if(taskSubjects.containsKey(task.getId())){
            taskSubjects.get(task.getId()).setValue(task);
        }
        allTasksSubject.setValue(getTasks());
    }


}
