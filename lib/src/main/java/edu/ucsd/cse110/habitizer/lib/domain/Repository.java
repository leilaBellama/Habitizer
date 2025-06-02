package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.util.Subject;

/**
 * Interface for accessing and storing in database
 */
public interface Repository {
    Integer countTasks();

    Task getTask(int id);
    Subject<Task> findTask(int id);

    Subject<List<Task>> findAllTasks();

    void saveTask(Task task);

    void saveTasks(List<Task> tasks);

    void removeTask(int id);

    Integer countRoutines();

    Routine getRoutine(int id);
    Subject<Routine> findRoutine(int id);

    Subject<List<Routine>> findAllRoutines();

    void saveRoutine(Routine routine);

    void saveRoutines(List<Routine> routines);

    void removeRoutine(int id);
}
