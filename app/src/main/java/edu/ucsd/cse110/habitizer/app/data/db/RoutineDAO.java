package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Implementation of data accessing object to get and store data in persistent database
 */
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
    int updateRoutine(RoutineEntity routine);

    @Query("SELECT COUNT(*) FROM routines_table WHERE id = :routineId")
    int containsRoutine(int routineId);

    //task functions
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertTask(TaskEntity task);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertTasks(List<TaskEntity> tasks);

    @Query("SELECT COUNT(*) FROM tasks_table")
    int countTasks();

    @Query("SELECT * FROM tasks_table WHERE id = :id")
    TaskEntity findTask(int id);

    @Query("SELECT * FROM tasks_table")
    List<TaskEntity> findAllTasks();

    @Query("SELECT * FROM tasks_table WHERE id = :id")
    LiveData<TaskEntity> findTaskAsLiveData(int id);

    @Query("SELECT * FROM tasks_table ORDER BY position ASC")
    LiveData<List<TaskEntity>> findAllTasksAsLiveData();

    @Query("DELETE FROM tasks_table WHERE id = :id")
    void deleteTask(int id);

    @Update
    int updateTask(TaskEntity task);

}
