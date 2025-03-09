package edu.ucsd.cse110.habitizer.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import edu.ucsd.cse110.habitizer.app.data.db.HabitizerDatabase;
import edu.ucsd.cse110.habitizer.app.data.db.RoomRoutineRepository;
import edu.ucsd.cse110.habitizer.app.data.db.RoutineDAO;
import edu.ucsd.cse110.habitizer.app.data.db.RoutineEntity;
import edu.ucsd.cse110.habitizer.lib.domain.OriginalTask;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineBuilder;
import edu.ucsd.cse110.habitizer.lib.domain.SimpleTaskBuilder;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.util.Subject;

public class RoomRepositoryTest {
    private HabitizerDatabase database;
    private RoomRoutineRepository repository;
    private RoutineDAO routineDao;

    public final static List<Task> tasks = List.of(
            new OriginalTask(null, "Morning Task 1",true),
            new OriginalTask(null, "Morning Task 2",true),
            new OriginalTask(null, "Morning Task 3",true),
            new OriginalTask(null, "Evening Task 1",false),
            new OriginalTask(null, "Evening Task 2",false),
            new OriginalTask(null, "Evening Task 3",false),
            new OriginalTask(null, "Evening Task 4",false)

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

    @Before
    public void setUp(){
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, HabitizerDatabase.class)
                .allowMainThreadQueries() // Only for testing
                .build();
        routineDao = database.routineDao();
        repository = new RoomRoutineRepository(routineDao);
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void testFind(){
        assertNull(routineDao.findRoutine(0));
        var routine0 = new RoutineBuilder()
                .setId(0)
                .setName("routine0")
                .buildRoutine();
        repository.saveRoutine(routine0);
        //var newRoutineId = routine0.getId();
        assertEquals("routine0",routineDao.findRoutine(0).name);
        //assertEquals(1,(int)newRoutineId);
        /*
        routineDao.findRoutineAsLiveData(newRoutineId).observe(getLifecycleOwner(),routine -> {
            assertNotNull(routine);
            assertEquals("routine0", routine.name);
        });

         */
        //assertEquals("routine0",routineDao.findRoutineAsLiveData(newRoutineId).getValue().name);
        assertNull(routineDao.findTask(0));
        assertNull(routineDao.findTask(1));

        var task0 = new SimpleTaskBuilder()
                .setId(0)
                .setTaskName("task0")
                .setRoutineId(0)
                .buildSimpleTask();
        var task1 = new SimpleTaskBuilder()
                .setId(1)
                .setTaskName("task1")
                .setRoutineId(0)
                .buildSimpleTask();
        repository.saveTask(task0);
        repository.saveTask(task1);
        assertEquals("task0",routineDao.findTask(0).name);
        assertEquals("task1",routineDao.findTask(1).name);
    }

    //save null id, nonnull id, already existing id
    @Test
    public void testSave(){

        var routine1 = new RoutineBuilder()
                .setId(1)
                .setName("routine1")
                .buildRoutine();
        var routine2 = new RoutineBuilder()
                .setId(2)
                .setName("routine2")
                .buildRoutine();
        var routine3 = new RoutineBuilder()
                .setName("routine3")
                .buildRoutine();
        repository.saveRoutine(routine1);
        repository.saveRoutine(routine2);
        repository.saveRoutine(routine3);

        assertEquals("routine1",routineDao.findRoutine(1).name);
        assertNull(routineDao.findRoutine(1).hasStarted);
        assertNull(routineDao.findRoutine(1).elapsedMinutes);
        assertNull(routineDao.findRoutine(1).goalTime);
        assertNull(routineDao.findRoutine(1).elapsedSeconds);

        assertEquals("routine2",routineDao.findRoutine(2).name);
        assertEquals("routine3",routineDao.findRoutine(3).name);

        var routine4 = new RoutineBuilder()
                .setId(1)
                .setHasStarted(true)
                .setName("routine4")
                .setElapsedSeconds(1)
                .setElapsedMinutes(2)
                .buildRoutine();
        repository.saveRoutine(routine4);
        assertEquals("routine4",routineDao.findRoutine(1).name);
        assertEquals(true,routineDao.findRoutine(1).hasStarted);
        assertEquals(1,(int) routineDao.findRoutine(1).elapsedSeconds);
        assertEquals(2,(int) routineDao.findRoutine(1).elapsedMinutes);
        assertEquals(3,(int) repository.countRoutines());

        var task1 = new SimpleTaskBuilder()
                .setId(1)
                .setTaskName("task1")
                .setRoutineId(1)
                .buildSimpleTask();
        var task2 = new SimpleTaskBuilder()
                .setRoutineId(2)
                .setTaskName("task2")
                .buildSimpleTask();
        var task3 = new SimpleTaskBuilder()
                .setRoutineId(3)
                .setTaskName("task3")
                .buildSimpleTask();
        var task4 = new SimpleTaskBuilder()
                .setRoutineId(9)
                .setTaskName("task4")
                .buildSimpleTask();
        assertEquals(1,(int)routine1.getId());

        repository.saveTask(task1);
        repository.saveTask(task2);
        repository.saveTask(task3);
        repository.saveTask(task4);
        assertEquals("task1",routineDao.findTask(1).name);
        assertEquals("task2",routineDao.findTask(2).name);
        assertEquals("task3",routineDao.findTask(3).name);
        assertNull(routineDao.findTask(4));
        assertEquals(3,(int) repository.countTasks());

        var task5 = new SimpleTaskBuilder()
                .setId(2)
                .setRoutineId(1)
                .setTaskName("task5")
                .buildSimpleTask();
        repository.saveTask(task5);
        assertEquals("task5",routineDao.findTask(2).name);


        for(Task task : tasks){
            repository.saveTask(task);
        }
        assertEquals(10,(int) repository.countTasks());
    }

    @Test
    public void testSaveAndRemoveTask(){
        repository.saveRoutine(new RoutineBuilder()
                .setId(0)
                .setName("routine0")
                .buildRoutine());
        repository.saveRoutine(new RoutineBuilder()
                .setId(1)
                .setName("routine1")
                .buildRoutine());
        for(Task task : tasks){
            repository.saveTask(task);
        }
        assertEquals(7,(int) repository.countTasks());
        repository.removeTask(4);
        assertEquals(6,(int) repository.countTasks());

    }

    @Test
    public void testLiveData() throws InterruptedException {
        // Create and insert multiple routines
        var routine1 = new RoutineBuilder()
                .setId(1)
                .setName("routine1")
                .buildRoutine();
        var routine2 = new RoutineBuilder()
                .setId(2)
                .setName("routine2")
                .buildRoutine();

        repository.saveRoutine(routine1);
        repository.saveRoutine(routine2);
        // Observe routine1
        Routine observedRoutine1 = LiveDataTestUtil.getOrAwaitValue(repository.findRoutineAsLiveData(1));
        Routine observedRoutine2 = LiveDataTestUtil.getOrAwaitValue(repository.findRoutineAsLiveData(2));

        // Modify routine1
        Routine updatedRoutine1 = new RoutineBuilder()
                .setId(1)
                .setName("updated routine1")
                .buildRoutine();
        repository.saveRoutine(updatedRoutine1);

        // Get updated values
        Routine newObservedRoutine1 = LiveDataTestUtil.getOrAwaitValue(repository.findRoutineAsLiveData(1));
        Routine newObservedRoutine2 = LiveDataTestUtil.getOrAwaitValue(repository.findRoutineAsLiveData(2));
        assertEquals("Updated Morning Routine", newObservedRoutine1.getName());
        assertEquals("Night Routine", newObservedRoutine2.getName()); // Should remain unchanged

    }

    public class LiveDataTestUtil {
        public static <T> T getOrAwaitValue(final LiveData<T> liveData) throws InterruptedException {
            final Object[] data = new Object[1];
            CountDownLatch latch = new CountDownLatch(1);
            Observer<T> observer = new Observer<T>() {
                @Override
                public void onChanged(T t) {
                    data[0] = t;
                    latch.countDown();
                    liveData.removeObserver(this);
                }
            };
            liveData.observeForever(observer);
            if (!latch.await(2, TimeUnit.SECONDS)) {
                throw new InterruptedException("LiveData value was never set.");
            }
            return (T) data[0];
        }
    }

}
