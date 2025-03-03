package edu.ucsd.cse110.habitizer.app;

import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import edu.ucsd.cse110.habitizer.lib.util.Subject;

public class RoutineTimer {
    private ScheduledExecutorService scheduler;
    private int elapsedSeconds;
    private Subject<Boolean> hasStarted;
    private Subject<Integer> elapsedMin;

    private Integer interval;

    public RoutineTimer(Integer interval) {
        this.hasStarted = new Subject<>();
        this.elapsedMin = new Subject<>();
        this.interval = interval;

        hasStarted.setValue(false);
    }

    public RoutineTimer(Integer interval,ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
        this.hasStarted = new Subject<>();
        this.elapsedMin = new Subject<>();
        this.interval = interval;
        this.elapsedMin.setValue(0);
        this.elapsedSeconds = 0;
        this.hasStarted.setValue(false);
    }

    public Subject<Integer> getElapsedMinutes() {
        return elapsedMin;
    }

    public int getElapsedSeconds() {
        return elapsedSeconds;
    }

    public Subject<Boolean> getHasStarted() {
        return hasStarted;
    }

    public void setTime(Integer minutes, int seconds) {
        this.getElapsedMinutes().setValue(minutes);
        this.elapsedSeconds = seconds;
    }

    public void advanceTime(Integer advance) {
        if (!hasStarted.getValue()) return;
        elapsedSeconds += advance;
        if (elapsedSeconds >= interval) {
            elapsedMin.setValue(elapsedMin.getValue()+1);
            elapsedSeconds -= interval;
        }
        Log.d("m","Advanced by 15 seconds minutes: " + getElapsedMinutes().getValue() + " seconds " + elapsedSeconds);
    }

    public void start() {
        //if (hasStarted.getValue()) return;
        if (scheduler == null || scheduler.isShutdown()) { // Create a new instance if shutdown
            scheduler = Executors.newScheduledThreadPool(1);
        }
        elapsedMin.setValue(0);
        elapsedSeconds = 0;
        hasStarted.setValue(true);
        scheduler.scheduleWithFixedDelay(() -> {
            elapsedSeconds++;
            if (elapsedSeconds >= interval) {
                elapsedMin.setValue(elapsedMin.getValue() + 1);
                elapsedSeconds = 0;
                Log.d("m","Elapsed time: " + getElapsedMinutes().getValue() + " minutes,");
            }
            Log.d("s","Elapsed time: " + elapsedSeconds  + " seconds");
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void stop() {
        scheduler.shutdown();
        Log.d("m", "Timer stopped at: " + getElapsedMinutes().getValue() + " minutes");
    }

    public void end() {
        stop();
        int roundUp = 0;
        if (elapsedSeconds > 0) { roundUp = 1;}
        elapsedMin.setValue(elapsedMin.getValue() + roundUp);
//        Log.d("m","Ended at: " + getElapsedTime().getValue() + " minutes");
    }
}
