package edu.ucsd.cse110.habitizer.lib.domain;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.List;
import java.util.Optional;

public class RoutineTest {

    @Test
    public void testBuilderSet(){
        List<Task> tasks = List.of(
                new OriginalTask(0, "Morning Task 1",true),
                new OriginalTask(1, "Morning Task 2",true),
                new OriginalTask(2, "Morning Task 3",true),
                new OriginalTask(3, "Evening Task 1",false),
                new OriginalTask(4, "Evening Task 2",false),
                new OriginalTask(5, "Evening Task 3",false),
                new OriginalTask(null, "Evening Task 4",false),
                new OriginalTask(null, "Evening Task 5",false)
        );
        RoutineBuilder builder = new RoutineBuilder();
        Routine r = new RoutineBuilder()
                .setId(1)
                .setName("new routine")
                .setHasStarted(false)
                .setElapsedTime(10)
                .setTasks(tasks)
                .buildRoutine();

        assertEquals(Optional.ofNullable(r.getId()), Optional.of(1));
        assertEquals(r.getName(),"new routine");
        assertEquals(r.getHasStarted(),false);
        assertEquals(Optional.ofNullable(r.getElapsedTime()), Optional.of(10));
        assertEquals(r.getTasks().size(),8);

    }
}
