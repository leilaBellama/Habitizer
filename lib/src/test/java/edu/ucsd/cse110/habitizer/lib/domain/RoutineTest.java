package edu.ucsd.cse110.habitizer.lib.domain;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.List;
import java.util.Optional;

public class RoutineTest {

    @Test
    public void testBuilderSet(){
        List<Task> tasks = List.of(
                new OriginalTask(0, "Morning Task 1",true,1),
                new OriginalTask(1, "Morning Task 2",true,2),
                new OriginalTask(2, "Morning Task 3",true,3),
                new OriginalTask(3, "Evening Task 1",false,1),
                new OriginalTask(4, "Evening Task 2",false,2),
                new OriginalTask(5, "Evening Task 3",false,3),
                new OriginalTask(null, "Evening Task 4",false,4),
                new OriginalTask(null, "Evening Task 5",false,5)
        );
        RoutineBuilder builder = new RoutineBuilder();
        Routine r = new RoutineBuilder()
                .setId(1)
                .setName("new routine")
                .setHasStarted(false)
                .setGoalTime("30")
                .buildRoutine();

        assertEquals(Optional.ofNullable(r.getId()), Optional.of(1));
        assertEquals(r.getName(),"new routine");
        assertEquals(r.getHasStarted(),false);
        assertEquals(r.getGoalTime(),"30");

    }
}
