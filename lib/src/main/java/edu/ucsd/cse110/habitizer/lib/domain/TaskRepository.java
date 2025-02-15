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

        public Integer count() {
        return dataSource.getTasks().size();
    }

    public Subject<Task> find(int id){
        return dataSource.getTaskSubject(id);
    }

    public Subject<List<Task>> findAll(){
        return dataSource.getAllTasksSubject();
    }

    public void save(Task task) {
        dataSource.putTask(task);
    }

    public void remove(int id) {dataSource.removeTask(id);}


    /*
    //evening methods

    public Subject<List<Subject<List<Task>>>> getBoth() {
        Subject<List<Subject<List<Task>>>> liveData = new Subject<>();

        List<Subject<List<Task>>> both = new ArrayList<>();
        both.add(dataSource.getAllTasksSubjectMorning());
        both.add(dataSource.getAllTasksSubjectEvening());
        liveData.setValue(both);
        return liveData;
    }
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
    


    public void appendEvening(Task task){
        int lastId = dataSource.getTasksEvening().size();
        task.setId(lastId);
        System.out.println("New Id" + task.getId());
        dataSource.putTaskEvening(task);
    }

     */
}
