package edu.ucsd.cse110.habitizer.app;


import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;


import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@RunWith(MockitoJUnitRunner.class)
public class TimerTest {

    private ScheduledExecutorService mockScheduler;
    private RoutineTimer routineTimer;
    private RoutineTimer routineTimer2;
    private Runnable capturedRunnable;


    @Before
    public void setUp() {
        // Mock the scheduler
        mockScheduler = Mockito.mock(ScheduledExecutorService.class);
        // Create RoutineTimer with the mock scheduler
        routineTimer = new RoutineTimer(1, mockScheduler); // 1-second interval

        routineTimer2 = new RoutineTimer(60);
        routineTimer2.getHasStarted().setValue(true);
        routineTimer2.getElapsedTime().setValue(0);
    }

    @Test
    public void testElapsedTimeUpdatesCorrectly() {

        // Capture the runnable that gets scheduled
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        routineTimer.start();

        // Verify that scheduleWithFixedDelay() was called with a Runnable
        verify(mockScheduler).scheduleWithFixedDelay(
                runnableCaptor.capture(), anyLong(), anyLong(), any(TimeUnit.class)
        );

        // Retrieve the captured runnable
        Runnable capturedRunnable = runnableCaptor.getValue();

        // Simulate multiple time intervals passing
        capturedRunnable.run();
        assertEquals(1, (int) routineTimer.getElapsedTime().getValue());

        capturedRunnable.run();
        assertEquals(2, (int) routineTimer.getElapsedTime().getValue());

        capturedRunnable.run();
        assertEquals(3, (int) routineTimer.getElapsedTime().getValue());

        // Stop the timer (if needed)
        routineTimer.stop();
    }

    @Test
    public void testAdvanceTimeUpdatesCorrectly() {

        // Capture the runnable that gets scheduled
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        routineTimer.start();

        // Verify that scheduleWithFixedDelay() was called with a Runnable
        verify(mockScheduler).scheduleWithFixedDelay(
                runnableCaptor.capture(), anyLong(), anyLong(), any(TimeUnit.class)
        );

        // Retrieve the captured runnable
        Runnable capturedRunnable = runnableCaptor.getValue();

        // Simulate multiple time intervals passing
        capturedRunnable.run();
        assertEquals(1, (int) routineTimer.getElapsedTime().getValue());

        capturedRunnable.run();
        assertEquals(2, (int) routineTimer.getElapsedTime().getValue());

        capturedRunnable.run();
        assertEquals(3, (int) routineTimer.getElapsedTime().getValue());

        // Stop the timer (if needed)
        routineTimer.stop();
    }

    @Test
    public void testAdvanceTimeCrossesInterval() {
        routineTimer2.advanceTime(60); // Advance by 60 seconds

        // Since 60 seconds = 1 minute, elapsedTime should increase by 1
        assertEquals(1, (int) routineTimer2.getElapsedTime().getValue());
    }

    @Test
    public void testAdvanceTimeAccumulatesProperly() {
        routineTimer2.advanceTime(30); // Advance by 30 seconds
        assertEquals(0, (int) routineTimer2.getElapsedTime().getValue());
        assertEquals(30, (int) routineTimer2.getElapsedSeconds());

        routineTimer2.advanceTime(30); // Advance another 30 seconds (total = 60)
        assertEquals(1, (int) routineTimer2.getElapsedTime().getValue());
        assertEquals(0, (int) routineTimer2.getElapsedSeconds());


        routineTimer2.advanceTime(90); // Advance by 90 seconds (should add 1.5 minutes)
        assertEquals(2, (int) routineTimer2.getElapsedTime().getValue()); // 1 full minute added
        assertEquals(30, (int) routineTimer2.getElapsedSeconds());

    }

}



