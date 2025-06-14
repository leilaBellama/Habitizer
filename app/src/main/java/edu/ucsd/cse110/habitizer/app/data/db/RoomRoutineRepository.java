package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import java.util.List;
import java.util.stream.Collectors;
import edu.ucsd.cse110.habitizer.app.util.LiveDataSubjectAdapter;
import edu.ucsd.cse110.habitizer.lib.domain.Repository;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.util.Subject;

public class RoomRoutineRepository implements Repository {
    private final RoutineDAO routineDao;

    public RoomRoutineRepository(RoutineDAO routineDAO){
        this.routineDao = routineDAO;
    }
    @Override
    public Integer countTasks() {
        return routineDao.countTasks();
    }

    @Override
    public Task getTask(int id) {
        var task = routineDao.findTask(id);
        if(task == null) return null;
        return routineDao.findTask(id).toTask();
    }
    @Override
    public Subject<Task> findTask(int id) {
        var entityLiveData = routineDao.findTaskAsLiveData(id);
        var taskLiveData = Transformations.map(entityLiveData, TaskEntity::toTask);
        return new LiveDataSubjectAdapter<>(taskLiveData);
    }

    @Override
    public Subject<List<Task>> findAllTasks() {
        var entitiesLiveData = routineDao.findAllTasksAsLiveData();
        var tasksLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(TaskEntity::toTask)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(tasksLiveData);
    }

    //check that routineId exists first
    @Override
    public void saveTask(Task task) {
        if(containsRoutine(task.getRoutineId())){
            TaskEntity entity = TaskEntity.fromTask(task);
            if (entity.id == null) {
                long id = routineDao.insertTask(entity);
                task.setId((int) id);
            } else {
                int result = routineDao.updateTask(entity);
            }
        }
    }

    @Override
    public void saveTasks(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) return;
        for(Task task: tasks){
            TaskEntity entity = TaskEntity.fromTask(task);
            routineDao.insertTask(entity);
        }
    }

    @Override
    public void removeTask(int id) {
        routineDao.deleteTask(id);
    }

    @Override
    public Integer countRoutines() {
        return routineDao.countRoutines();
    }

    @Override
    public Routine getRoutine(int id) {
        var routine = routineDao.findRoutine(id);
        if(routine == null) return null;
        return routineDao.findRoutine(id).toRoutine();
    }

    @Override
    public Subject<Routine> findRoutine(int id) {
        var entityLiveData = routineDao.findRoutineAsLiveData(id);
        var routineLiveData = Transformations.map(entityLiveData, RoutineEntity::toRoutine);
        return new LiveDataSubjectAdapter<>(routineLiveData);
    }

    public LiveData<Routine> findRoutineAsLiveData(int id) {
        var entityLiveData = routineDao.findRoutineAsLiveData(id);
        return Transformations.map(entityLiveData, RoutineEntity::toRoutine);
    }

    @Override
    public Subject<List<Routine>> findAllRoutines() {
        var entitiesLiveData = routineDao.findAllRoutinesAsLiveData();
        var routinesLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(RoutineEntity::toRoutine)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(routinesLiveData);
    }

    @Override
    public void saveRoutine(Routine routine) {
        RoutineEntity entity = RoutineEntity.fromRoutine(routine);
        if (entity.id == null) {
            long id = routineDao.insertRoutine(entity);
            routine.setId((int) id);
        } else {
            int result = routineDao.updateRoutine(entity);
        }
    }

    @Override
    public void saveRoutines(List<Routine> routines) {
        if (routines == null || routines.isEmpty()) return;
        var entities = routines.stream()
                .map(RoutineEntity::fromRoutine)
                .collect(Collectors.toList());
        routineDao.insertRoutines(entities);
    }

    @Override
    public void removeRoutine(int id) {
        routineDao.deleteRoutine(id);
    }

    public boolean containsRoutine(int routineId){
        return routineDao.containsRoutine(routineId) > 0;
    }
}
