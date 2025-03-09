package edu.ucsd.cse110.habitizer.app.data.db;

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
        //android.util.Log.d("RoomRep ST", " contains " + containsRoutine(task.getRoutineId()));
        if(containsRoutine(task.getRoutineId())){
            android.util.Log.d("RoomRep ST", task.getId() + task.getName()+  " contains routine id " + task.getRoutineId() + containsRoutine(task.getRoutineId()));
            TaskEntity entity = TaskEntity.fromTask(task);
            long id = routineDao.insertTask(entity);
//            task.setId((int) id);
            android.util.Log.d("RoomRep ST", "new task id: " + id);
            /*
            if (entity.id == null) {
                long id = routineDao.insertTask(entity);
                task.setId((int) id);
                android.util.Log.d("RoomRep ST", "new task null id: " + task.getId());
            } else {
                int result = routineDao.updateTask(entity);
                    android.util.Log.d("RoomRep ST","added new task old id");
            }

             */
        }
    }

    @Override
    public void saveTasks(List<Task> tasks) {
        if (tasks == null) return;
        for (int i = 0; i < tasks.size(); i++) {
            saveTask(tasks.get(i));
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
    public Subject<Routine> findRoutine(int id) {
        var entityLiveData = routineDao.findRoutineAsLiveData(id);
        var routineLiveData = Transformations.map(entityLiveData, RoutineEntity::toRoutine);
        return new LiveDataSubjectAdapter<>(routineLiveData);
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
        routineDao.insertRoutine(entity);

        /*
        if (entity.id == null) {
            long id = routineDao.insertRoutine(entity);
            routine.setId((int) id);
            android.util.Log.d("RoomRep SR", "new routine " + routine.getId());

        } else {
            int result = routineDao.updateRoutine(entity);
            android.util.Log.d("RoomRep SR", "Updated routine " + routine.getId() + ", result: " + result);
        }

         */
    }

    @Override
    public void saveRoutines(List<Routine> routines) {

        if (routines == null || routines.isEmpty()) return;
        var entities = routines.stream()
                .map(RoutineEntity::fromRoutine)
                .collect(Collectors.toList());
        routineDao.insertRoutines(entities);
        /*
        List<Long> insertedIds = routineDao.insertRoutines(entities);
        for (int i = 0; i < routines.size(); i++) {
            if (routines.get(i).getId() == null) {
                routines.get(i).setId(insertedIds.get(i).intValue());
            }
        }

         */
    }

    @Override
    public void removeRoutine(int id) {
        routineDao.deleteRoutine(id);
    }

    public boolean containsRoutine(int routineId){
        return routineDao.containsRoutine(routineId) > 0;
    }
}
