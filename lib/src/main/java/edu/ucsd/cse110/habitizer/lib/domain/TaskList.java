package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class TaskList {
    /*
    private final List<Task> tasks;

    public TaskList(List<Task> tasks){
        this.tasks = List.copyOf(tasks);
    }


    public static List<Task> addTask(List<Task> list, Task task){
        if(list == null) return null;
        var size = list.size();
        var newTask = new SimpleTaskBuilder(task).setId()
                task.withId(size);
        var copy = new ArrayList<>(List.copyOf(list));
        copy.add(newTask);
        return copy;
    }

     */

    public static List<Task> resetAll(List<Task> list){
        if(list == null) return null;
        var copy = new ArrayList<Task>();
        for (int i = 0; i < list.size(); i++) {
            Task task = new SimpleTaskBuilder(list.get(i)).setCheckedOff(false).setCheckedOffTime(null).buildSimpleTask();
            copy.add(task);
        }
        return copy;
    }

    /*
    public static List<Task> removeTask(List<Task> list, int id){
        if(list == null) return null;
        var copy = new ArrayList<>(List.copyOf(list));
        for (var task : copy) {
            if(task.getId() == id){
                copy.remove(task);
            }
        }
        return copy;
    }


    public static List<Task> editTaskName(List<Task> list, int id, String newName) {
        if(list == null) return null;
        var copy = new ArrayList<>(List.copyOf(list));
        for (int i = 0; i < copy.size(); i++) {
            if(copy.get(i).getId() == id){
                copy.get(i).setName(newName);
            }
        }
        return copy;
    }

     */
}