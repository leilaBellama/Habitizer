package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface Task {
    @Nullable
    Integer getId();

    void setId(int id);
    Integer getRoutineId();

    void setRoutineId(int id);

    @NonNull
    String getTaskName();

    boolean getCheckedOffStatus();

    Integer getCheckedOffTime();

    void setName(String taskName);

    // Does not support unchecking, adds another if statement
    // to prevent checkoff
    void setCheckedOff(boolean isChecked, Integer checkedOffTime);

    // Use this function for resetting after end routine
    void reset();

    //for testing purpose
    Task withId(int id);


}
