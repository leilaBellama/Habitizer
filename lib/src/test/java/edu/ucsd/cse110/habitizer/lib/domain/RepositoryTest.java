package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;

public class RepositoryTest {
    InMemoryDataSource dataSource = new InMemoryDataSource();
    Repository repository = new SimpleRepository(dataSource);

    public final static List<Task> tasks = List.of(
            new SimpleTask(0, "Morning Task 1"),
            new SimpleTask(1, "Morning Task 2"),
            new SimpleTask(2, "Morning Task 3"),
            new SimpleTask(null, "Morning Task 4"),
            new SimpleTask(null, "Morning Task 5"),
            new SimpleTask(null, "Evening Task 1"),
            new SimpleTask(null, "Evening Task 2"),
            new SimpleTask(null, "Evening Task 3"),
            new SimpleTask(null, "Evening Task 4"),
            new SimpleTask(null, "Monday Task 1"),
            new SimpleTask(null, "Monday Task 2"),
            new SimpleTask(null, "Tuesday Task 1"),
            new SimpleTask(null, "Tuesday Task 2"),
            new SimpleTask(null, "Tuesday Task 3")

    );

    public final static List<Routine> routines = List.of(
            new RoutineBuilder()
                    .setId(null)
                    .setName("Morning")
                    .setHasStarted(false)
                    .buildRoutine(),

            new RoutineBuilder()
                    .setId(null)
                    .setName("Evening")
                    .setHasStarted(false)
                    .buildRoutine(),
            new RoutineBuilder()
                    .setId(null)
                    .setName("Monday")
                    .setHasStarted(false)
                    .buildRoutine()

    );

    @Test
    public void testSaveRoutineAndRemoveRoutine(){
        var data = new InMemoryDataSource();
        repository.saveRoutines(routines);
        repository.saveRoutine(new RoutineBuilder()
                .setId(null)
                .setName("Tuesday")
                .setHasStarted(false)
                .buildRoutine());
        assertEquals(4,(int) repository.countRoutines());
        repository.removeRoutine(2);
        assertEquals(3,(int) repository.countRoutines());

        assertEquals(repository.findRoutine(1).getValue().getName(), "Morning");
        assertEquals(repository.findRoutine(3).getValue().getName(), "Monday");
        assertEquals(repository.findRoutine(4).getValue().getName(), "Tuesday");

        var routine = repository.findRoutine(1).getValue();

        assertNotNull(repository.findRoutine(1).getValue().getHasStarted());
        assertNotNull(routine);
        routine.setHasStarted(null);
        routine.setElapsedMinutes(null);
        routine.setElapsedSeconds(null);

        repository.saveRoutine(routine);
        assertNull(repository.findRoutine(1).getValue().getHasStarted());

    }


    @Test
    public void testSaveAndRemoveTask(){
        for(Task task : tasks){
            repository.saveTask(task);
        }
        assertEquals(14,(int) repository.countTasks());
        repository.removeTask(4);
        assertEquals(13,(int) repository.countTasks());

    }

}
