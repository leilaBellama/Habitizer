package edu.ucsd.cse110.habitizer.lib.domain;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;

public class InMemoryDataSourceTest {

    public final static List<Task> tasks = List.of(
            new OriginalTask(0, "Morning Task 1",true),
            new OriginalTask(1, "Morning Task 2",true),
            new OriginalTask(2, "Morning Task 3",true),
            new OriginalTask(3, "Evening Task 1",false),
            new OriginalTask(4, "Evening Task 2",false),
            new OriginalTask(5, "Evening Task 3",false),
            new OriginalTask(null, "Evening Task 4",false),
            new OriginalTask(null, "Evening Task 5",false)
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
                    .buildRoutine()
    );

    @Test
    public void testPutAndRemoveRoutine(){
        var data = new InMemoryDataSource();
        for(Routine routine : routines){
            data.putRoutine(routine);
        }
        assertEquals(data.getRoutines().size(),2);
        assertEquals(data.getRoutine(0).getName(),"Morning");
        data.putRoutine(new RoutineBuilder()
                .setId(null)
                .setName("Monday")
                .setHasStarted(false)
                .buildRoutine());
        assertEquals(data.getRoutines().size(),3);
        assertEquals(data.getRoutine(2).getName(),"Monday");

        data.removeRoutine(0);
        assertEquals(data.getRoutines().size(),2);
        assertNull(data.getRoutine(0));

    }



}
