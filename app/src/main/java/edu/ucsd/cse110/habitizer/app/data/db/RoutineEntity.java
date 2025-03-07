package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineBuilder;

@Entity(tableName = "routines_table")
public class RoutineEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id = null;

    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "hasStarted")
    public Boolean hasStarted;
    @ColumnInfo(name = "elapsedMinutes")
    public Integer elapsedMinutes;
    @ColumnInfo(name = "elapsedSeconds")
    public Integer elapsedSeconds;

    @ColumnInfo(name = "goalTime")
    public String goalTime;

    RoutineEntity(String name, Boolean hasStarted, Integer elapsedMinutes, Integer elapsedSeconds, String goalTime){
        this.name = name;
        this.elapsedMinutes = elapsedMinutes;
        this.elapsedSeconds = elapsedSeconds;
        this.hasStarted = hasStarted;
        this.goalTime = goalTime;
    }

    public static RoutineEntity fromRoutine(@NonNull Routine routine){
        var newRoutine = new RoutineEntity(routine.getName(),routine.getHasStarted(), routine.getElapsedMinutes(), routine.getElapsedMinutes(), routine.getGoalTime());

        newRoutine.id = routine.getId();
        return newRoutine;
    }

    public @NonNull Routine toRoutine(){
        return new RoutineBuilder()
                .setId(id)
                .setName(name)
                .setHasStarted(hasStarted)
                .setElapsedMinutes(elapsedMinutes)
                .setElapsedSeconds(elapsedSeconds)
                .setGoalTime(goalTime)
                .buildRoutine();
    }


}
