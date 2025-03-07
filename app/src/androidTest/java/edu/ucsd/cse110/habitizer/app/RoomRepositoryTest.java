package edu.ucsd.cse110.habitizer.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import edu.ucsd.cse110.habitizer.app.data.db.HabitizerDatabase;
import edu.ucsd.cse110.habitizer.app.data.db.RoomRoutineRepository;
import edu.ucsd.cse110.habitizer.app.data.db.RoutineDAO;
import edu.ucsd.cse110.habitizer.app.data.db.RoutineEntity;
import edu.ucsd.cse110.habitizer.app.data.db.TaskEntity;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.OriginalTask;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineBuilder;
import edu.ucsd.cse110.habitizer.lib.domain.SimpleTask;
import edu.ucsd.cse110.habitizer.lib.domain.SimpleTaskBuilder;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

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
    public void testSaveAndRemove(){

        repository.saveRoutine(new RoutineBuilder()
                .setId(null)
                .setName("Morning")
                .setHasStarted(false)
                .buildRoutine());
        repository.saveTask(new SimpleTaskBuilder()
                .setId(null)
                .setTaskName("Tuesday")
                .setCheckedOff(false)
                .setRoutineId(0)
                .buildSimpleTask());
        repository.saveRoutine(new RoutineBuilder()
                .setId(null)
                .setName("evening")
                .setHasStarted(false)
                .buildRoutine());
        /*
        var num = routineDao.findAllRoutines().size();
        var siz = routineDao.countRoutines();
        android.util.Log.d("RRTest", "routines  " + num + " size " + siz);
        var routine = routineDao.findRoutine(0);
*/
        /*
        repository.saveTask(new SimpleTaskBuilder()
                .setId(null)
                .setTaskName("Tuesday")
                .setCheckedOff(false)
                .setRoutineId(0)
                .buildSimpleTask());
        assertEquals(2,(int) repository.countRoutines());
        assertEquals(1,(int) repository.countTasks());

        //repository.saveRoutines(routines);
        //assertEquals(4,(int) repository.countRoutines());
        repository.saveTasks(tasks);
        var a = repository.findAllRoutines().getValue();
        var b = repository.findAllTasks().getValue();
        var c = repository.findTask(0).getValue();
        var d = repository.findTask(1).getValue();
        var e = repository.findTask(2).getValue();
        var f = repository.findTask(3).getValue();
        var g = repository.findTask(4).getValue();
        assertEquals(8,(int) repository.countTasks());



        //repository.removeRoutine(0);
        //repository.removeTask(0);
        //assertEquals(0,(int) repository.countRoutines());
        //assertEquals(0,(int) repository.countTasks());


        /*


        var data = new InMemoryDataSource();
        repository.saveRoutines(routines);
        repository.saveRoutine(new RoutineBuilder()
                .setId(null)
                .setName("Tuesday")
                .setHasStarted(false)
                .buildRoutine());
        assertEquals(4,(int) repository.countRoutines());
        repository.removeRoutine(1);
        assertEquals(3,(int) repository.countRoutines());

        assertEquals(repository.findRoutine(0).getValue().getName(), "Morning");
        assertEquals(repository.findRoutine(2).getValue().getName(), "Monday");
        assertEquals(repository.findRoutine(3).getValue().getName(), "Tuesday");

        var routine = repository.findRoutine(0).getValue();

        assertNotNull(repository.findRoutine(0).getValue().getHasStarted());
        assertNotNull(routine);
        routine.setHasStarted(null);
        routine.setElapsedMinutes(null);
        routine.setElapsedSeconds(null);

        repository.saveRoutine(routine);
        assertNull(repository.findRoutine(0).getValue().getHasStarted());

         */

    }


    /*
    @Test
    public void testSaveAndRemoveTask(){
        for(Task task : tasks){
            repository.saveTask(task);
        }
        assertEquals(14,(int) repository.countTasks());
        repository.removeTask(4);
        assertEquals(13,(int) repository.countTasks());

    }

     */
}
