package edu.ucsd.cse110.habitizer.app;

import junit.framework.TestCase;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.OriginalTask;
import edu.ucsd.cse110.habitizer.lib.domain.Repository;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineBuilder;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineTimer;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

public class MainViewModelTest extends TestCase {

    InMemoryDataSource dataSource;
    Repository rep;
    MainViewModel mvm;

    public final static List<Task> Morning = List.of(
            new OriginalTask(0, "Morning Task 1",true),
            new OriginalTask(1, "Morning Task 2",true),
            new OriginalTask(2, "Morning Task 3",true),
            new OriginalTask(3, "Morning Task 4",true),
            new OriginalTask(4, "Morning Task 5",true)
    );

    public final static List<Task> Evening = List.of(
            new OriginalTask(0, "Evening Task 1",false),
            new OriginalTask(1, "Evening Task 2",false),
            new OriginalTask(2, "Evening Task 3",false),
            new OriginalTask(3, "Evening Task 4",false)
    );
    public final static List<Routine> routines = List.of(
            new RoutineBuilder()
                    .setId(null)
                    .setName("Morning")
                    //.setHasStarted(null)
                    //.setElapsedMinutes(0)
                    //.setElapsedSeconds(0)
                    .setTasks(Morning)
                    .setGoalTime("35")
                    //.setTimer(new RoutineTimer(60))
                    .buildRoutine(),

            new RoutineBuilder()
                    .setId(null)
                    .setName("Evening")
                    //.setHasStarted(null)
                    //.setElapsedMinutes(0)
                    //.setElapsedSeconds(0)
                    .setTasks(Evening)
                    .setGoalTime("30")
                    //.setTimer(new RoutineTimer(60))
                    .buildRoutine()
    );

    public void setUp() throws Exception {
        dataSource = new InMemoryDataSource();
        rep = new Repository(dataSource);
        rep.save(routines);
        mvm = new MainViewModel(rep);
        super.setUp();
    }

    public void testStartRoutine() {

        var id = mvm.getRoutineId().getValue();
        var repository = mvm.getRepository();
        var routine = repository.find(id).getValue();
        assertNull(routine.getHasStarted());
        assertNull(routine.getElapsedMinutes());
        assertNull(routine.getTimer());

        mvm.startRoutine();
        assertTrue(routine.getHasStarted().booleanValue());
        assertNotNull(routine.getTimer());

    }

    public void testEndRoutine() {
        var id = mvm.getRoutineId().getValue();
        var repository = mvm.getRepository();
        var routine = repository.find(id).getValue();
        mvm.startRoutine();
        mvm.endRoutine();
        assertFalse(routine.getHasStarted());
        assertNotNull(routine.getElapsedMinutes());
        assertNotNull(routine.getElapsedSeconds());
        assertNotNull(routine.getTimer());

    }
    public void testAddTask() {
        var id = mvm.getRoutineId().getValue();
        var repository = mvm.getRepository();
        var routine = repository.find(id).getValue();
        assertEquals(5,repository.find(0).getValue().getTasks().size());
        assertEquals(4,repository.find(1).getValue().getTasks().size());

        mvm.addTask(new OriginalTask(null,"new morning task",true));
        mvm.swapRoutine();
        mvm.addTask(new OriginalTask(null,"new evening task",false));

        assertEquals(6,repository.find(0).getValue().getTasks().size());
        assertEquals(5,repository.find(1).getValue().getTasks().size());
    }
    public void testStopTimer() {
    }

    public void testAdvanceTime() {
    }

    public void testSwapRoutine() {
    }



    public void testSetTaskName() {
    }

    public void testSetGoalTime() {
    }
}