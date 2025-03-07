package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class RoutineWithTasks {
    @Embedded
    public RoutineEntity routine;

    @Relation(parentColumn = "id",entityColumn = "routineId")
    public List<TaskEntity> tasks;
}
