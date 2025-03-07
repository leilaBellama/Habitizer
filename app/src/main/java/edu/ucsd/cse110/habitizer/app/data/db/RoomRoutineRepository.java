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
        return 0;
    }

    @Override
    public Subject<Task> findTask(int id) {
        return null;
    }

    @Override
    public Subject<List<Task>> findAllTasks() {
        return null;
    }

    @Override
    public void saveTask(Task task) {

    }

    @Override
    public void saveTasks(List<Task> tasks) {

    }

    @Override
    public void removeTask(int id) {

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
        if (entity.id == null) {
            long id = routineDao.insert(entity);
            routine.setId((int) id);
        } else {
            int result = routineDao.update(entity);
            // Add this log to debug update operations
            android.util.Log.d("RoomRoutineRepository", "Updated routine " + entity.id + ", result: " + result);
        }
    }

    @Override
    public void saveRoutines(List<Routine> routines) {
        if (routines == null || routines.isEmpty()) return;
        var entities = routines.stream()
                .map(RoutineEntity::fromRoutine)
                .collect(Collectors.toList());
        List<Long> insertedIds = routineDao.insert(entities);
        for (int i = 0; i < routines.size(); i++) {
            if (routines.get(i).getId() == null) {
                routines.get(i).setId(insertedIds.get(i).intValue());
            }
        }
    }

    @Override
    public void removeRoutine(int id) {
        routineDao.deleteRoutine(id);
    }
}
