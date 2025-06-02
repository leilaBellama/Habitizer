package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class for ordered list of tasks
 */
public class TaskList {
    private final List<Task> tasks;

    public TaskList(List<Task> tasks){
        this.tasks = List.copyOf(tasks);
    }

    public static List<Task> addTask(List<Task> list, Task task){
        if(list == null || task == null) return null;
        var tasks = new ArrayList<Task>(list);
        tasks.add(task);
        return tasks;
    }

    public static List<Task> resetAll(List<Task> list){
        if(list == null) return null;
        var copy = new ArrayList<Task>();
        for (int i = 0; i < list.size(); i++) {
            Task task = list.get(i);
            task.setCheckedOff(false);
            task.setCheckedOffTime(null);
            copy.add(task);
        }
        return copy;
    }

    public static List<Task> removeTask(List<Task> list, int id){
        if(list == null) return null;
        var copy = new ArrayList<>(List.copyOf(list));
        copy.removeIf(task -> task.getId() == id);
        return copy;
    }
    public static List<Task> editTaskName(List<Task> list, int id, String newName) {
        if(list == null) return null;
        var copy = new ArrayList<Task>(list);
        for (var task : copy) {
            if(task.getId() == id){
                task.setName(newName);
            }
        }
        return copy;
    }

    public static List<Task> editCheckedOff(List<Task> list, int id, boolean checkedOff) {
        if(list == null) return null;
        var copy = new ArrayList<Task>(list);
        for (var task : copy) {
            if(task.getId() == id){
                task.setCheckedOff(checkedOff);
            }
        }
        return copy;
    }

    public static List<Task> editCheckedOffTime(List<Task> list, int id, String checkOffTime) {
        if(list == null) return null;
        var copy = new ArrayList<Task>(list);
        for (var task : copy) {
            if(task.getId() == id){
                task.setCheckedOffTime(checkOffTime);
            }
        }
        return copy;
    }
}