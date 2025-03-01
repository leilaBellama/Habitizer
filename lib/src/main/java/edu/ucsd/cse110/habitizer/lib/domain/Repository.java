package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.util.Subject;
public class Repository {
    private final InMemoryDataSource dataSource;

    public Repository(InMemoryDataSource dataSource){
        this.dataSource = dataSource;
    }

    /*

    public Integer countTasksWithRoutineId(Integer routineId) {
        return dataSource.getTasksWithRoutineId(routineId).size();
    }

    public Subject<List<Task>> findAllTasksWithRoutineID(Integer routineID){
        return dataSource.getAllTasksSubjectWithRoutineId(routineID);
    }


    public Integer countTasks() {
        return dataSource.getTasks().size();
    }

    public Subject<Task> findTask(int id){
        return dataSource.getTaskSubject(id);
    }

    public Subject<List<Task>> findAllTasks(){
        return dataSource.getAllTasksSubject();
    }

    public void saveTask(Task task) {dataSource.putTask(task);}
    public void saveTasks(List<Task> tasks) {
        for(var task : tasks){
            dataSource.putTask(task);
        }
    }
    //

    public void removeTask(int id) {dataSource.removeTask(id);}

    //public void editTaskName(int id, String name) {dataSource.editTask(id, name);}

     */


    public Integer count() {return dataSource.getRoutines().size();}

    public Subject<Routine> find(int id) {return dataSource.getRoutineSubject(id);}
    public Subject<List<Routine>> findAll() {return dataSource.getAllRoutinesSubject();}

    public void save(Routine routine) {dataSource.putRoutine(routine);}
    public void save(List<Routine> routines) {
        for(var routine : routines){
            dataSource.putRoutine(routine);
        }
    }

    public void remove(int id) {dataSource.removeRoutine(id);}

}
