package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.util.Subject;
public class TaskRepository {
    private final InMemoryDataSource dataSource;

    public TaskRepository(InMemoryDataSource dataSource){
        this.dataSource = dataSource;
    }

    public Subject<List<Subject<List<Task>>>> getBoth() {
        Subject<List<Subject<List<Task>>>> liveData = new Subject<>();

        List<Subject<List<Task>>> both = new ArrayList<>();
        both.add(dataSource.getAllTasksSubjectMorning());
        both.add(dataSource.getAllTasksSubjectEvening());
        liveData.setValue(both);
        return liveData;
    }

    //morning methods

    public Integer count() {
        return dataSource.getTasksMorning().size();
    }

    public Subject<Task> find(int id){
        return dataSource.getTaskSubjectMorning(id);
    }

    public Subject<List<Task>> findAll(){
        return dataSource.getAllTasksSubjectMorning();
    }

    public void save(Task task) {
        dataSource.putTaskMorning(task);
    }

    //evening methods
    public Integer countEvening() {
        return dataSource.getTasksEvening().size();
    }

    public Subject<Task> findEvening(int id){
        return dataSource.getTaskSubjectEvening(id);
    }

    public Subject<List<Task>> findAllEvening(){
        return dataSource.getAllTasksSubjectEvening();
    }

    public void saveEvening(Task task) {
        dataSource.putTaskEvening(task);
    }
}
