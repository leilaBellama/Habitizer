package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.util.Subject;
public class SimpleRepository implements Repository {
    private final InMemoryDataSource dataSource;

    public SimpleRepository(InMemoryDataSource dataSource){
        this.dataSource = dataSource;
    }

    /*

    public Integer countTasksWithRoutineId(Integer routineId) {
        return dataSource.getTasksWithRoutineId(routineId).size();
    }

    public Subject<List<Task>> findAllTasksWithRoutineID(Integer routineID){
        return dataSource.getAllTasksSubjectWithRoutineId(routineID);
    }

     */

    @Override
    public Integer countTasks() {
        return dataSource.getTasks().size();
    }

    @Override
    public Task getTask(int id){
        return dataSource.getTaskSubject(id).getValue();
    }
    @Override
    public Subject<Task> findTask(int id){
        return dataSource.getTaskSubject(id);
    }

    @Override
    public Subject<List<Task>> findAllTasks(){
        return dataSource.getAllTasksSubject();
    }

    @Override
    public void saveTask(Task task) {dataSource.putTask(task);}
    @Override
    public void saveTasks(List<Task> tasks) {
        for(var task : tasks){
            dataSource.putTask(task);
        }
    }

    @Override
    public void removeTask(int id) {dataSource.removeTask(id);}

    @Override
    public Integer countRoutines() {return dataSource.getRoutines().size();}

    @Override
    public Routine getRoutine(int id) {return dataSource.getRoutineSubject(id).getValue();}
    @Override
    public Subject<Routine> findRoutine(int id) {return dataSource.getRoutineSubject(id);}
    @Override
    public Subject<List<Routine>> findAllRoutines() {return dataSource.getAllRoutinesSubject();}

    @Override
    public void saveRoutine(Routine routine) {dataSource.putRoutine(routine);}
    @Override
    public void saveRoutines(List<Routine> routines) {
        for(var routine : routines){
            dataSource.putRoutine(routine);
        }
    }

    @Override
    public void removeRoutine(int id) {dataSource.removeRoutine(id);}

}
