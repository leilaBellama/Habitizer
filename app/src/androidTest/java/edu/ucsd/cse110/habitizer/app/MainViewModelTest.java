package edu.ucsd.cse110.habitizer.app;


import androidx.test.ext.junit.runners.AndroidJUnit4;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.OriginalTask;
import edu.ucsd.cse110.habitizer.lib.domain.Repository;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineBuilder;
import edu.ucsd.cse110.habitizer.lib.domain.Task;


//@RunWith(AndroidJUnit4.class)
//public class MainViewModelTest {

//    InMemoryDataSource dataSource;
//    Repository rep;
//    MainViewModel mvm;
//
//    Integer id;
//
//    Repository repository;
//
//
//    public final static List<Task> Morning = List.of(
//            new OriginalTask(0, "Morning Task 1",true),
//            new OriginalTask(1, "Morning Task 2",true),
//            new OriginalTask(2, "Morning Task 3",true),
//            new OriginalTask(3, "Morning Task 4",true),
//            new OriginalTask(4, "Morning Task 5",true)
//    );
//
//    public final static List<Task> Evening = List.of(
//            new OriginalTask(0, "Evening Task 1",false),
//            new OriginalTask(1, "Evening Task 2",false),
//            new OriginalTask(2, "Evening Task 3",false),
//            new OriginalTask(3, "Evening Task 4",false)
//    );
//    public final static List<Routine> routines = List.of(
//            new RoutineBuilder()
//                    .setId(null)
//                    .setName("Morning")
//                    .setTasks(Morning)
//                    .setGoalTime("35")
//                    .buildRoutine(),
//
//            new RoutineBuilder()
//                    .setId(null)
//                    .setName("Evening")
//                    .setTasks(Evening)
//                    .setGoalTime("30")
//                    .buildRoutine()
//    );
//
//    @Before
//    public void setUp() throws Exception {
//        dataSource = new InMemoryDataSource();
//        rep = new Repository(dataSource);
//        rep.save(routines);
//        mvm = new MainViewModel(rep);
//        mvm.setRoutineId(0);
//        id = mvm.getRoutineId().getValue();
//        repository = mvm.getRepository();
//        super.setUp();
//    }
//
//    @Test
//    //before switching routines, after switching, and to new routine
//    public void testStartRoutine() {
//        var routine = repository.find(id).getValue();
//        assertNull(routine.getHasStarted());
//        assertNull(routine.getElapsedMinutes());
//
//        mvm.startRoutine();
//        assertTrue(routine.getHasStarted().booleanValue());
//
//    }
//
//    @Test
//    public void testEndRoutine() {
//        var routine = repository.find(id).getValue();
//        mvm.startRoutine();
//        mvm.endRoutine();
//        assertFalse(routine.getHasStarted());
//        assertNotNull(routine.getElapsedMinutes());
//        assertNotNull(routine.getElapsedSeconds());
//
//    }
//
//    @Test
//    public void testNewRoutine(){
//        assertEquals(2,mvm.getRoutines().getValue().size());
//        mvm.newRoutine();
//        assertEquals(3,mvm.getRoutines().getValue().size());
//        assertEquals("New Routine",repository.find(2).getValue().getName());
//
//    }
//
//    @Test
//    //before switching routines, after switching, and to new routine
//    public void testAddTask() {
//        assertEquals(5,repository.find(0).getValue().getTasks().size());
//        assertEquals(4,repository.find(1).getValue().getTasks().size());
//
//        mvm.addTask(new OriginalTask(null,"new morning task",true));
//        mvm.setRoutineId(1);
//        mvm.addTask(new OriginalTask(null,"new evening task",false));
//
//        mvm.newRoutine();
//        mvm.setRoutineId(2);
//        assertEquals(0,repository.find(2).getValue().getTasks().size());
//
//        mvm.addTask(new OriginalTask(null,"new task",false));
//        assertEquals(6,repository.find(0).getValue().getTasks().size());
//        assertEquals(5,repository.find(1).getValue().getTasks().size());
//        assertEquals(1,repository.find(2).getValue().getTasks().size());
//
//    }
//
//    @Test
//    public void testStopTimer() {
//        assertNull(mvm.getElapsedTime().getValue());
//        mvm.startRoutine();
//        mvm.stopTimer();
//        assertEquals(0,(int)mvm.getElapsedTime().getValue());
//    }
//
//    @Test
//    public void testAdvanceTime() {
//
//        mvm.startRoutine();
//        mvm.stopTimer();
//        mvm.advanceTime();
//
//        assertEquals(0,(int)repository.find(id).getValue().getElapsedMinutes());
//        mvm.advanceTime();
//        mvm.advanceTime();
//        mvm.advanceTime();
//        mvm.advanceTime();
//        assertEquals(1,(int)repository.find(id).getValue().getElapsedMinutes());
//        assertEquals(15,(int)repository.find(id).getValue().getElapsedSeconds());
//
//    }
//
//    @Test
//    public void testSetTaskName() {
//        assertEquals("Morning Task 1",repository.find(id).getValue().getTasks().get(0).getTaskName());
//
//        mvm.setTaskName(0, "new name");
//        assertEquals("new name",repository.find(id).getValue().getTasks().get(0).getTaskName());
//    }
//
//    @Test
//    public void testSetGoalTime() {
//        assertEquals("35",repository.find(id).getValue().getGoalTime());
//
//        mvm.setGoalTime("5");
//        assertEquals("5",repository.find(id).getValue().getGoalTime());
//
//    }
//}