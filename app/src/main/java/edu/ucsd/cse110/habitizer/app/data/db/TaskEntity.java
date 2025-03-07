package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

@Entity(tableName = "Routines")
public class TaskEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id = null;

    @ColumnInfo(name = "taskName")
    public String taskName;
    @ColumnInfo(name = "checkedOff")
    public Boolean checkedOff;
    @ColumnInfo(name = "routineId")
    public Integer routineId;
    @ColumnInfo(name = "checkedOffTime")
    public Integer checkedOffTime;

    TaskEntity(@NonNull String taskName, Boolean checkedOff, Integer checkedOffTime,Integer routineId){
        this.taskName = taskName;
        this.checkedOff = checkedOff;
        this.checkedOffTime = checkedOffTime;
        this.routineId = routineId;
    }

    public static TaskEntity fromTask(@NonNull Task task) {
        var taskEntity = new TaskEntity(
                task.getTaskName(),
                task.getCheckedOffStatus(),
                task.getCheckedOffTime(),
                task.getRoutineId()
        );
        taskEntity.id = task.getId();
        return taskEntity;
    }

    /*
    public @NonNull Routine toRoutine(){
        return new Routine(id,name,hasStarted,elapsedMinutes,elapsedSeconds,goalTime);
    }

     */


}
