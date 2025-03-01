package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;

public class RepositoryTest {
    InMemoryDataSource dataSource = new InMemoryDataSource();
    TaskRepository repository = new TaskRepository(dataSource);

    public final static List<Task> tasks = List.of(
            new SimpleTask(0, "Morning Task 1",0),
            new SimpleTask(1, "Morning Task 2",0),
            new SimpleTask(2, "Morning Task 3",0),
            new SimpleTask(null, "Morning Task 4",0),
            new SimpleTask(null, "Morning Task 5",0),
            new SimpleTask(null, "Evening Task 1",1),
            new SimpleTask(null, "Evening Task 2",1),
            new SimpleTask(null, "Evening Task 3",1),
            new SimpleTask(null, "Evening Task 4",1),
            new SimpleTask(null, "Monday Task 1",2),
            new SimpleTask(null, "Monday Task 2",2),
            new SimpleTask(null, "Tuesday Task 1",3),
            new SimpleTask(null, "Tuesday Task 2",3),
            new SimpleTask(null, "Tuesday Task 3",3)

    );
    public final static List<Routine> routines = List.of(
            new RoutineBuilder()
                    .setId(null)
                    .setName("Morning")
                    .setHasStarted(false)
                    .setElapsedTime(0)
                    .buildRoutine(),

            new RoutineBuilder()
                    .setId(null)
                    .setName("Evening")
                    .setHasStarted(false)
                    .setElapsedTime(0)
                    .buildRoutine(),
            new RoutineBuilder()
                    .setId(null)
                    .setName("Monday")
                    .setHasStarted(false)
                    .setElapsedTime(0)
                    .buildRoutine()

    );

    @Test
    public void testSaveAndRemoveRoutine(){
        var data = new InMemoryDataSource();
        repository.saveRoutines(routines);
        repository.saveRoutine(new RoutineBuilder()
                .setId(null)
                .setName("Tuesday")
                .setHasStarted(false)
                .setElapsedTime(0)
                .setTasks(tasks)
                .buildRoutine());
        assertEquals(4,(int) repository.countRoutines());
        repository.removeRoutine(1);
        assertEquals(3,(int) repository.countRoutines());

        assertEquals(repository.findRoutine(0).getValue().getName(), "Morning");
        assertEquals(repository.findRoutine(2).getValue().getName(), "Monday");
        assertEquals(repository.findRoutine(3).getValue().getName(), "Tuesday");
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

    @Test
    public void testGetRoutinesTasks(){
        for(Task task : tasks){
            repository.saveTask(task);
        }
        assertEquals(5,(int) repository.countTasksWithRoutineId(0));
        assertEquals(4,(int) repository.countTasksWithRoutineId(1));
        assertEquals(2,(int) repository.countTasksWithRoutineId(2));
        assertEquals(3,(int) repository.countTasksWithRoutineId(3));
        repository.saveTask(new SimpleTask(0, "Morning Task first",0));
        assertEquals("Morning Task first",repository.findTask(0).getValue().getTaskName());
    }


}
