package edu.ucsd.cse110.habitizer.app;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.util.Log;

import edu.ucsd.cse110.habitizer.lib.util.Subject;

public class RoutineTimer {
    private ScheduledExecutorService scheduler;
    private int elapsedSeconds;
    private Subject<Boolean> hasStarted;
    private Subject<Integer> elapsedTime;

    private Integer interval;

    public RoutineTimer(Integer interval) {
        this.hasStarted = new Subject<>();
        this.elapsedTime = new Subject<>();
        this.interval = interval;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();

        elapsedTime.setValue(0);
        hasStarted.setValue(false);
    }

    public RoutineTimer(Integer interval,ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
        this.hasStarted = new Subject<>();
        this.elapsedTime = new Subject<>();
        this.interval = interval;
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

    public void setTime(Integer minutes, int seconds) {
        this.getElapsedTime().setValue(minutes);
        this.elapsedSeconds = seconds;
    }

    public void advanceTime(Integer advance) {
        if (!hasStarted.getValue()) return;
        elapsedSeconds += advance; // Increase elapsed seconds
        if (elapsedSeconds >= interval) {
            elapsedTime.setValue(elapsedTime.getValue()+1);
            elapsedSeconds -= interval;
        }
        //Log.d("m","Advanced by 30 seconds. New time: " + getElapsedTime().getValue() + " minutes, ");
    }

    public void start() {
        if (this.hasStarted.getValue()) return;
        this.hasStarted.setValue(true);
        //Log.d("s","started ");

        scheduler.scheduleWithFixedDelay(() -> {
            elapsedSeconds++;
            if (elapsedSeconds >= interval) {
                elapsedTime.setValue(elapsedTime.getValue() + 1);
                elapsedSeconds = 0;
                //Log.d("m","Elapsed time: " + getElapsedTime().getValue() + " minutes,");
            }
            //Log.d("s","Elapsed time: " + elapsedSeconds  + " seconds");
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void stop() {
        scheduler.shutdown();
    }
}
