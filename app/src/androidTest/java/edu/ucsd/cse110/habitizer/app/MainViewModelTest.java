package edu.ucsd.cse110.habitizer.app;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.OriginalTask;
import edu.ucsd.cse110.habitizer.lib.domain.Repository;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineBuilder;
import edu.ucsd.cse110.habitizer.lib.domain.SimpleRepository;
import edu.ucsd.cse110.habitizer.lib.domain.SimpleTask;
import edu.ucsd.cse110.habitizer.lib.domain.Task;


@RunWith(AndroidJUnit4.class)
public class MainViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    InMemoryDataSource dataSource;
    Repository rep;
    MainViewModel mvm;
    Integer id;
    Repository repository;

    public final static List<Task> Morning = List.of(
            new SimpleTask(0, "Morning Task 1", false, null, 1, 1),
            new SimpleTask(1, "Morning Task 2", false, null, 1, 2),
            new SimpleTask(2, "Morning Task 3", false, null, 1, 3),
            new SimpleTask(3, "Morning Task 4", false, null, 1, 4),
            new SimpleTask(4, "Morning Task 5", false, null, 1, 5)
    );

    public final static List<Task> Evening = List.of(
            new SimpleTask(0, "Evening Task 1", false, null, 2, 1),
            new SimpleTask(1, "Evening Task 2", false, null, 2, 2),
            new SimpleTask(2, "Evening Task 3", false, null, 2, 3),
            new SimpleTask(3, "Evening Task 4", false, null, 2, 4)
    );
    public final static List<Routine> routines = List.of(
            new RoutineBuilder()
                    .setId(null)
                    .setName("Morning")
                    .setTasks(Morning)
                    .setGoalTime("35")
                    .buildRoutine(),

            new RoutineBuilder()
                    .setId(null)
                    .setName("Evening")
                    .setTasks(Evening)
                    .setGoalTime("30")
                    .buildRoutine()
    );

    @Before
    public void setUp() {
        dataSource = new InMemoryDataSource();
        rep = new SimpleRepository(dataSource);
        rep.saveRoutines(routines);
        mvm = new MainViewModel(rep);
        mvm.setRoutineId(1);
        id = mvm.getRoutineId().getValue();
        repository = mvm.getRepository();
        mvm.updateTaskOrder(Morning);
    }

    @Test
    public void testStartRoutine() {
        var routine = rep.findRoutine(id).getValue();
        Assert.assertNull(routine.getHasStarted());
        Assert.assertNull(routine.getElapsedMinutes());

        mvm.startRoutine();
        Assert.assertTrue(routine.getHasStarted());
        Assert.assertNotNull(routine.getElapsedMinutes());
    }

    @Test
    public void testResetRoutine() {
        mvm.setRoutineId(id);
        mvm.updateTaskOrder(Morning);

        List<Task> tasksBefore = mvm.getOrderedTasks().getValue();
        Assert.assertNotNull(tasksBefore);
        for (Task task : tasksBefore) {
            Assert.assertFalse(task.getCheckedOffStatus());
            Assert.assertNull(task.getCheckedOffTime());
        }

        List<Task> modifiedTasks = new ArrayList<>(tasksBefore);
        modifiedTasks.get(0).setCheckedOff(true);
        modifiedTasks.get(1).setCheckedOffTime("12 min");
        mvm.updateTaskOrder(modifiedTasks);

        mvm.reset();

        var routine = mvm.getRoutine().getValue();

        Assert.assertNull(mvm.getHasStarted().getValue());
        Assert.assertNull(routine.getHasStarted());
        Assert.assertNull(routine.getElapsedMinutes());
        Assert.assertNull(routine.getElapsedSeconds());
        Assert.assertNull(routine.getCurrentTaskTime());

        List<Task> tasksAfter = mvm.getOrderedTasks().getValue();
        for (Task task : tasksAfter) {
            Assert.assertFalse(task.getCheckedOffStatus());
            Assert.assertNull(task.getCheckedOffTime());
        }
    }

    @Test
    public void testEndRoutine() {
        var routine = mvm.getRoutine().getValue();
        mvm.startRoutine();
        mvm.endRoutine();
        Assert.assertFalse(routine.getHasStarted());
        Assert.assertNotNull(routine.getElapsedMinutes());
        Assert.assertNotNull(routine.getElapsedSeconds());
        Assert.assertNotNull(routine.getCurrentTaskTime());
    }

    @Test
    public void testNewRoutine() {
        Assert.assertEquals(2, mvm.getRoutines().getValue().size());
        mvm.newRoutine();
        Assert.assertEquals(3, mvm.getRoutines().getValue().size());
        Assert.assertEquals("New Routine", repository.findRoutine(3).getValue().getName());
    }

    @Test
    public void testAddTask() {
        mvm.updateTaskOrder(Morning);
        Assert.assertEquals(5, mvm.getOrderedTasks().getValue().size());
        mvm.addTask(new SimpleTask(5, "New Morning Task", false, null, 1, 6));
        Assert.assertEquals(6, mvm.getOrderedTasks().getValue().size());
        Assert.assertEquals("New Morning Task", mvm.getOrderedTasks().getValue().get(5).getName());
    }

    @Test
    public void testStopTimer() {
        mvm.startRoutine();
        mvm.stopTimer();
        Assert.assertEquals(0, (int) mvm.getElapsedTime().getValue());
    }

    @Test
    public void testAdvanceTime() {
        mvm.startRoutine();
        mvm.stopTimer();
        mvm.advanceTime();

        Assert.assertEquals(0, (int)mvm.getElapsedTime().getValue());
        mvm.advanceTime();
        mvm.advanceTime();
        mvm.advanceTime();
        mvm.advanceTime();
        Assert.assertEquals(1,(int)mvm.getElapsedTime().getValue());
    }

    @Test
    public void testSetTaskName() {
        mvm.updateTaskOrder(Morning);
        Assert.assertEquals("Morning Task 1", repository.findRoutine(id).getValue().getTasks().get(0).getName());
        mvm.setTaskName(0, "new name");
        Assert.assertEquals("new name",repository.findRoutine(id).getValue().getTasks().get(0).getName());
    }

    @Test
    public void testSetGoalTime() {
        Assert.assertEquals("35",repository.findRoutine(id).getValue().getGoalTime());
        mvm.setGoalTime("5");
        Assert.assertEquals("5",repository.findRoutine(id).getValue().getGoalTime());
    }

    @Test
    public void testSetRoutineName() {
        mvm.setRoutineName("Test");
        Assert.assertEquals("Test", repository.getRoutine(id).getName());
    }

    @Test
    public void testRemoveTask() {
        mvm.updateTaskOrder(Morning);
        Assert.assertEquals(5, mvm.getOrderedTasks().getValue().size());
        Assert.assertEquals("Morning Task 1", mvm.getOrderedTasks().getValue().get(0).getName());
        mvm.removeTask(0);
        Assert.assertEquals(4, mvm.getOrderedTasks().getValue().size());
        Assert.assertEquals("Morning Task 2", mvm.getOrderedTasks().getValue().get(0).getName());
    }
}