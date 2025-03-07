package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RoutineDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(RoutineEntity routine);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(List<RoutineEntity> routines);

    @Query("SELECT COUNT(*) FROM routines")
    int countRoutines();

    @Query("SELECT * FROM routines WHERE id = :id")
    RoutineEntity findRoutine(int id);

    @Query("SELECT * FROM routines")
    List<RoutineEntity> findAllRoutines();

    @Query("SELECT * FROM routines WHERE id = :id")
    LiveData<RoutineEntity> findRoutineAsLiveData(int id);

    @Query("SELECT * FROM routines")
    LiveData<List<RoutineEntity>> findAllRoutinesAsLiveData();

    @Query("DELETE FROM routines WHERE id = :id")
    void deleteRoutine(int id);

    @Update
    int update(RoutineEntity routine);

}
