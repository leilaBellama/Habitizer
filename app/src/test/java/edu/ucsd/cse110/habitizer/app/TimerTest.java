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

    @Mock
    private ScheduledExecutorService mockExecutor;

    @Mock
    private ScheduledFuture<?> mockFuture;

    private RoutineTimer timer;
    private Runnable capturedTask;

    /*
    @Test
    public void StartSchedulerTest() {
        ScheduledExecutorService mockSchedulor = Mockito.mock(ScheduledExecutorService.class);
        RoutineTimer testTimer = Mockito.spy(new RoutineTimer(mockSchedulor));

        testTimer.start();
        Mockito.verify(mockSchedulor, times(1)).scheduleWithFixedDelay(Mockito.any(Runnable.class), anyLong(), anyLong(), Mockito.any(TimeUnit.class));
        assertEquals((int) testTimer.getElapsedTime().getValue(),0);
        testTimer.stop();
    }


    // Initialize mocks with openMocks

    @Before
    public void setUp() {

        try (var mocks = MockitoAnnotations.openMocks(this)) {  // try-with-resources
            // Capture the Runnable passed to scheduleAtFixedRate
            ArgumentCaptor<Runnable> taskCaptor = ArgumentCaptor.forClass(Runnable.class);

            ScheduledFuture mockFuture = mock(ScheduledFuture.class);

// Mock the scheduleAtFixedRate method
            when(mockExecutor.scheduleAtFixedRate(taskCaptor.capture(), anyLong(), anyLong(), any(TimeUnit.class)))
                    .thenReturn(mockFuture);

            // Initialize TimerManager with the mocked scheduler
            timer = new RoutineTimer(mockExecutor);

            // Start the timer which should schedule a task
            timer.start();

            // Retrieve the captured task
            capturedTask = taskCaptor.getValue();

            // Ensure the captured task is not null
            assertNotNull("Scheduled task should not be null", capturedTask);
        } catch (Exception e) {
            e.printStackTrace();  // In case of errors during setup
        }
        assertNotNull("Captured Runnable should not be null", capturedTask);


    }

    // Clean up mocks after test
    @After
    public void tearDown() throws Exception {
        // Close the mocks to release resources
        mockExecutor = null;
    }

    @Test
    public void testVerifyScheduleCalled() {
        // Verify that scheduleWithFixedDelay was called exactly once with the correct arguments
        verify(mockExecutor, times(1)).scheduleWithFixedDelay(any(Runnable.class), anyLong(), anyLong(), any(TimeUnit.class));

        // Ensure the captured task is not null before running it
        assertNotNull("Captured Runnable should not be null", capturedTask);
    }

    @Test
    public void testTimerIncrementsSeconds() {
        // Simulate the task running for 30 seconds
        for (int i = 0; i < 30; i++) {
            capturedTask.run();
        }

        // Verify that elapsed seconds are counted correctly
        assertEquals( 30, timer.getElapsedSeconds());
        assertEquals(0, (int) timer.getElapsedTime().getValue()); // Should still be 0
    }

    @Test
    public void testTimerIncrementsMinutes() {
        // Simulate the task running for 60 seconds
        for (int i = 0; i < 60; i++) {
            capturedTask.run();
        }

        // Verify that seconds reset and minutes increment
        assertEquals(0, timer.getElapsedSeconds()); // Should reset after 60 seconds
        assertEquals(1, (int) timer.getElapsedTime().getValue()); // 1 minute should have passed
    }
    */



}



