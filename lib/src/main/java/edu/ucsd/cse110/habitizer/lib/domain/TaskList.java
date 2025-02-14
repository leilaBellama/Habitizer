package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class TaskList {
    private final List<Task> tasks;

    public TaskList(List<Task> tasks){
        this.tasks = List.copyOf(tasks);
    }
}