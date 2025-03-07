package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.util.Subject;

public interface Repository {
    Integer countTasks();

    Subject<Task> findTask(int id);

    Subject<List<Task>> findAllTasks();

    void saveTask(Task task);

    void saveTasks(List<Task> tasks);

    void addTask(Task task);


    void removeTask(int id);

    Integer countRoutines();

    Subject<Routine> findRoutine(int id);

    Subject<List<Routine>> findAllRoutines();

    void saveRoutine(Routine routine);

    void saveRoutines(List<Routine> routines);

    void addRoutine(Routine routine);


    void removeRoutine(int id);
}
