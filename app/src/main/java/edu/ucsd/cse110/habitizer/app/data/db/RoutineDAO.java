package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RoutineDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertRoutine(RoutineEntity routine);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertRoutines(List<RoutineEntity> routines);

    @Query("SELECT COUNT(*) FROM routines_table")
    int countRoutines();

    @Query("SELECT * FROM routines_table WHERE id = :id")
    RoutineEntity findRoutine(int id);

    @Query("SELECT * FROM routines_table")
    List<RoutineEntity> findAllRoutines();

    @Query("SELECT * FROM routines_table WHERE id = :id")
    LiveData<RoutineEntity> findRoutineAsLiveData(int id);

    @Query("SELECT * FROM routines_table")
    LiveData<List<RoutineEntity>> findAllRoutinesAsLiveData();

    @Query("DELETE FROM routines_table WHERE id = :id")
    void deleteRoutine(int id);

    @Update
    int updateRoutines(RoutineEntity routine);

    //task functions
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertTask(TaskEntity routine);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertTasks(List<TaskEntity> routines);

    @Query("SELECT COUNT(*) FROM tasks_table")
    int countTasks();

    @Query("SELECT * FROM tasks_table WHERE id = :id")
    TaskEntity findTask(int id);

    @Query("SELECT * FROM tasks_table")
    List<TaskEntity> findAllTasks();

    @Query("SELECT * FROM tasks_table WHERE id = :id")
    LiveData<TaskEntity> findTaskAsLiveData(int id);

    @Query("SELECT * FROM tasks_table")
    LiveData<List<TaskEntity>> findAllTasksAsLiveData();

    @Query("DELETE FROM tasks_table WHERE id = :id")
    void deleteTask(int id);

    @Update
    int updateTasks(TaskEntity task);

}
