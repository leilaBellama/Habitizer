package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;

public class RepositoryTest {
    InMemoryDataSource dataSource = new InMemoryDataSource();
    Repository repository = new Repository(dataSource);

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
    public void testSaveAndRemoveRoutine(){
        var data = new InMemoryDataSource();
        repository.save(routines);
        repository.save(new RoutineBuilder()
                .setId(null)
                .setName("Tuesday")
                .setHasStarted(false)
                .setTasks(tasks)
                .buildRoutine());
        assertEquals(4,(int) repository.count());
        repository.remove(1);
        assertEquals(3,(int) repository.count());

        assertEquals(repository.find(0).getValue().getName(), "Morning");
        assertEquals(repository.find(2).getValue().getName(), "Monday");
        assertEquals(repository.find(3).getValue().getName(), "Tuesday");

        var routine = repository.find(0).getValue();

        assertNotNull(repository.find(0).getValue().getHasStarted());

        routine.setHasStarted(null);
        routine.setElapsedSeconds(null);
        routine.setElapsedMinutes(null);
        routine.setTasks(TaskList.resetAll(routine.getTasks()));
        repository.save(routine);
        assertNull(repository.find(0).getValue().getHasStarted());

    }

}
