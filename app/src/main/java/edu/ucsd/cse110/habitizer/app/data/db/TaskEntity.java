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

/**
 * Task object to put in database, child of Routine object
 */
@Entity(tableName = "tasks_table",
foreignKeys = @ForeignKey(
        entity = RoutineEntity.class,
        parentColumns = "id",
        childColumns = "routineId"),
        indices = {@Index(value = "routineId")}
)
public class TaskEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id = null;

    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "checkedOff")
    public Boolean checkedOff;
    @ColumnInfo(name = "routineId")
    public Integer routineId;
    @ColumnInfo(name = "checkedOffTime")
    public String checkedOffTime;
    @ColumnInfo(name = "position")
    public Integer position;

    public TaskEntity(@NonNull String name, Boolean checkedOff, String checkedOffTime, Integer routineId, Integer position){
        this.name = name;
        this.checkedOff = checkedOff;
        this.checkedOffTime = checkedOffTime;
        this.routineId = routineId;
        this.position = position;
    }

    public static TaskEntity fromTask(@NonNull Task task) {
        var taskEntity = new TaskEntity(
                task.getName(),
                task.getCheckedOffStatus(),
                task.getCheckedOffTime(),
                task.getRoutineId(),
                task.getPosition()
        );
        taskEntity.id = task.getId();
        return taskEntity;
    }

    public @NonNull Task toTask(){
        return new SimpleTaskBuilder()
                .setId(id)
                .setTaskName(name)
                .setCheckedOff(checkedOff)
                .setCheckedOffTime(checkedOffTime)
                .setRoutineId(routineId)
                .setPosition(position)
                .buildSimpleTask();
    }

}
