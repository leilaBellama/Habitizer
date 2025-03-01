package edu.ucsd.cse110.habitizer.lib.domain;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TimerTest {

    private RoutineTimer routineTimer;

    @Before
    public void setUp() {

        routineTimer = new RoutineTimer(60);
        routineTimer.getHasStarted().setValue(true);
        routineTimer.getElapsedMinutes().setValue(0);
    }

    @Test
    public void testAdvanceTimeCrossesInterval() {
        routineTimer.advanceTime(61); // Advance by 60 seconds

        // Since 60 seconds = 1 minute, elapsedTime should increase by 1
        assertEquals(1, (int) routineTimer.getElapsedMinutes().getValue());
    }

    @Test
    public void testAdvanceTimeAccumulatesProperly() {
        routineTimer.advanceTime(30); // Advance by 30 seconds
        assertEquals(0, (int) routineTimer.getElapsedMinutes().getValue());
        assertEquals(30, (int) routineTimer.getElapsedSeconds());

        routineTimer.advanceTime(30); // Advance another 30 seconds (total = 60)
        assertEquals(1, (int) routineTimer.getElapsedMinutes().getValue());
        assertEquals(0, (int) routineTimer.getElapsedSeconds());

        routineTimer.advanceTime(90); // Advance by 90 seconds (should add 1.5 minutes)
        assertEquals(2, (int) routineTimer.getElapsedMinutes().getValue()); // 1 full minute added
        assertEquals(30, (int) routineTimer.getElapsedSeconds());

    }
}



