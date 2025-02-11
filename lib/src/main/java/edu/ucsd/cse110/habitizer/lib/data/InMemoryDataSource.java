package edu.ucsd.cse110.habitizer.lib.data;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.util.Subject;
public class InMemoryDataSource {
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

    public InMemoryDataSource(){

    }

    public final static List<Task> MORNING_TASKS = List.of(
            new Task(0, "Morning Task 1"),
            new Task(1, "Morning Task 2"),
            new Task(2, "Morning Task 3")
    );
    public final static List<Task> EVENING_TASKS = List.of(
            new Task(0, "Evening Task 1"),
            new Task(1, "Evening Task 2"),
            new Task(2, "Evening Task 3")
    );

    public static InMemoryDataSource DEFAULT(){
        var data = new InMemoryDataSource();
        for(Task task : MORNING_TASKS){
            data.putTaskMorning(task);
        }
        for(Task task : EVENING_TASKS){
            data.putTaskEvening(task);
        }
        return data;
    }

    //morning methods
    public List<Task> getTasksMorning(){
        return List.copyOf(tasksMorning.values());
    }

    public Task getTaskMorning(int id){
        return tasksMorning.get(id);
    }

    public Subject<Task> getTaskSubjectMorning(int id){
        if(!taskSubjectsMorning.containsKey(id)){
            var subject = new Subject<Task>();
            subject.setValue(getTaskMorning(id));
            taskSubjectsMorning.put(id, subject);
        }
        return taskSubjectsMorning.get(id);
    }

    public Subject<List<Task>> getAllTasksSubjectMorning() {
        return allTasksSubjectMorning;
    }

    public void putTaskMorning(Task task){
        tasksMorning.put(task.getId(), task);
        if(taskSubjectsMorning.containsKey(task.getId())){
            taskSubjectsMorning.get(task.getId()).setValue(task);
        }
        allTasksSubjectMorning.setValue(getTasksMorning());
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

}
