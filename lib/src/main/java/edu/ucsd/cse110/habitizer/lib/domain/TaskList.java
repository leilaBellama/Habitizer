package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class TaskList {
    private final List<Task> tasks;

    public TaskList(List<Task> tasks){
        this.tasks = List.copyOf(tasks);
    }

    public static List<Task> addTask(List<Task> list, Task task){
        var size = list.size();
        var newTask = task.withId(size);
        var copy = new ArrayList<>(List.copyOf(list));
        copy.add(newTask);
        return copy;
    }

    public static List<Task> removeTask(List<Task> list, int id){
        var copy = new ArrayList<>(List.copyOf(list));
        copy.remove(id);
        for (int i = id; i < copy.size(); i++) {
            copy.get(i).setId(i); // Renumber IDs starting from 1
        }
        return copy;
    }
    public static List<Task> editTaskName(List<Task> list, int id, String newName) {
        var copy = new ArrayList<>(List.copyOf(list));
        copy.get(id).setName(newName);
        return copy;
    }
}