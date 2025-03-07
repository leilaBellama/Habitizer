package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.SimpleTask;
import edu.ucsd.cse110.habitizer.lib.domain.SimpleTaskBuilder;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

@Entity(tableName = "tasks_table",
foreignKeys = @ForeignKey(
        entity = RoutineEntity.class,
        parentColumns = "id",
        childColumns = "routineId",
        onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = "routineId")}
)
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

    public TaskEntity(@NonNull String taskName, Boolean checkedOff, Integer checkedOffTime, Integer routineId){
        this.taskName = taskName;
        this.checkedOff = checkedOff;
        this.checkedOffTime = checkedOffTime;
        this.routineId = routineId;
    }

    public static TaskEntity fromTask(@NonNull Task task) {
        var taskEntity = new TaskEntity(
                task.getName(),
                task.getCheckedOffStatus(),
                task.getCheckedOffTime(),
                task.getRoutineId()
        );
        taskEntity.id = task.getId();
        return taskEntity;
    }

    public @NonNull Task toTask(){
        return new SimpleTaskBuilder()
                .setId(id)
                .setTaskName(taskName)
                .setCheckedOff(checkedOff)
                .setCheckedOffTime(checkedOffTime)
                .setRoutineId(routineId)
                .buildSimpleTask();
    }

}
