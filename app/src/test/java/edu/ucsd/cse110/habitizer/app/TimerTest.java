package edu.ucsd.cse110.habitizer.app;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.Executors;

@RunWith(MockitoJUnitRunner.class)
public class TimerTest {

    private RoutineTimer routineTimer;

    @Before
    public void setUp() {

        routineTimer = new RoutineTimer(60, Executors.newScheduledThreadPool(1));
        routineTimer.getHasStarted().setValue(true);
        routineTimer.getElapsedMinutes().setValue(0);
    }

    @Test
    public void testAdvanceTimeCrossesInterval() {
        routineTimer.advanceTime(61);

        assertEquals(1, (int) routineTimer.getElapsedMinutes().getValue());
    }

    @Test
    public void testAdvanceTimeAccumulatesProperly() {
        routineTimer.advanceTime(30);
        assertEquals(0, (int) routineTimer.getElapsedMinutes().getValue());
        assertEquals(30, (int) routineTimer.getElapsedSeconds());

        routineTimer.advanceTime(30);
        assertEquals(1, (int) routineTimer.getElapsedMinutes().getValue());
        assertEquals(0, (int) routineTimer.getElapsedSeconds());

        routineTimer.advanceTime(90);
        assertEquals(2, (int) routineTimer.getElapsedMinutes().getValue());
        assertEquals(30, (int) routineTimer.getElapsedSeconds());

    }

    @Test
    public void testTimeStops() throws InterruptedException {
        routineTimer.advanceTime(30);
        routineTimer.stop();

        Thread.sleep(1000);

        assertEquals(0, (int) routineTimer.getElapsedMinutes().getValue());
        assertEquals(30, (int) routineTimer.getElapsedSeconds());
    }

    @Test
    public void testTimeEnds() throws InterruptedException {
        routineTimer.advanceTime(30);
        routineTimer.end();

        Thread.sleep(1000);

        assertEquals(1, (int) routineTimer.getElapsedMinutes().getValue());
    }
}



