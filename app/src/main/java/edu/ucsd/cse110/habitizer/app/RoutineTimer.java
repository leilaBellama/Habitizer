package edu.ucsd.cse110.habitizer.app;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.util.Log;

import edu.ucsd.cse110.habitizer.lib.util.Subject;

public class RoutineTimer {
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    //private ScheduledFuture<?> futureTask;
    private int elapsedSeconds;
    private Subject<Boolean> hasStarted;
    private Subject<Integer> elapsedTime;

    public RoutineTimer() {
        this.hasStarted = new Subject<>();
        this.elapsedTime = new Subject<>();

        this.elapsedTime.setValue(0);
        this.elapsedSeconds = 0;
        hasStarted.setValue(false);
    }

    public RoutineTimer(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
        this.hasStarted = new Subject<>();
        this.elapsedTime = new Subject<>();

        this.elapsedTime.setValue(0);
        this.elapsedSeconds = 0;
        hasStarted.setValue(false);
    }

    public Subject<Integer> getElapsedTime() {
        return elapsedTime;
    }

    public int getElapsedSeconds() {
        return elapsedSeconds;
    }

    public Subject<Boolean> getHasStarted() {
        return hasStarted;
    }

    public void setTime(Integer time) {
        this.elapsedSeconds = time;
    }

    public void advanceTime(Integer advance) {
        if (!hasStarted.getValue()) return;
        elapsedSeconds += advance; // Increase elapsed seconds
        if (elapsedSeconds >= 60) {
            elapsedTime.setValue(elapsedTime.getValue()+1);
            elapsedSeconds -= 60;
        }
        Log.d("m","Advanced by 30 seconds. New time: " + getElapsedTime().getValue() + " minutes, ");
    }

    public void start() {
        if (this.hasStarted.getValue()) return;
        this.hasStarted.setValue(true);

        /*
        if (futureTask != null && !futureTask.isCancelled()) {
            futureTask.cancel(false); // Cancel any existing task
        }

         */

        //futureTask = scheduler.scheduleWithFixedDelay(() -> {
        scheduler.scheduleWithFixedDelay(() -> {
            elapsedSeconds++;
            if (elapsedSeconds >= 60) {
                elapsedTime.setValue(elapsedTime.getValue() + 1);
                elapsedSeconds = 0;
                Log.d("m","Elapsed time: " + getElapsedTime().getValue() + " minutes,");
            }
            Log.d("s","Elapsed time: " + elapsedSeconds  + " seconds");
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void stop() {

        /*
        if (futureTask != null) {
            futureTask.cancel(false);
        }

         */
        scheduler.shutdown();
    }
}
