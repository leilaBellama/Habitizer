package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Interface for Task object, getters and setters for fields
 */
public interface Task {
    @Nullable
    Integer getId();

    void setId(int id);
    Integer getRoutineId();

    void setRoutineId(int id);

    @NonNull
    String getName();

    void setName(String taskName);

    void setPosition(Integer position);

    boolean getCheckedOffStatus();

    void setCheckedOff(boolean isChecked);

    String getCheckedOffTime();

    void setCheckedOffTime(String time);

    Integer getPosition();
}
