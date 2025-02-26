package edu.ucsd.cse110.habitizer.app;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.util.Log;

import edu.ucsd.cse110.habitizer.lib.util.Subject;
import edu.ucsd.cse110.observables.PlainMutableSubject;

public class RoutineTimer {
    private ScheduledExecutorService scheduler;
    private int elapsedSeconds;
    private Subject<Boolean> hasStarted;
    private Subject<Integer> elapsedTime;
    //private PlainMutableSubject<Integer> elapsedTime;

    private Integer interval;

    public RoutineTimer(Integer interval) {
        this.hasStarted = new Subject<>();
        this.elapsedTime = new Subject<>();
        //this.elapsedTime = new PlainMutableSubject<>();
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
        this.hasStarted.setValue(false);
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
        Log.d("m0","Advanced by 30 seconds. New time: " + getElapsedTime().getValue() + " minutes, ");
        if (!hasStarted.getValue()) return;
        Log.d("m","Advanced by 30 seconds. New time: " + getElapsedTime().getValue() + " minutes, ");

        elapsedSeconds += 55;
        if (elapsedSeconds >= interval) {
            elapsedTime.setValue(elapsedTime.getValue()+1);
            elapsedSeconds -= interval;
        }
        Log.d("m","Advanced by 30 seconds. New time: " + getElapsedTime().getValue() + " minutes, ");
    }

    public void start() {
        //Log.d("s","started ");
        //Log.d("s","hasStarted " + (this.hasStarted.getValue()));
        if (hasStarted.getValue()) return;
        hasStarted.setValue(true);
        //Log.d("s","hasStarted " + (hasStarted.getValue()));


        scheduler.scheduleWithFixedDelay(() -> {

            elapsedSeconds++;
            if (elapsedSeconds >= interval) {
                Log.d("t","reached interval elapsed time is null " + (this.elapsedTime.getValue() == null));
                this.elapsedTime.setValue(1);
                //elapsedTime.getValue() + 1);
                elapsedSeconds = 0;
                Log.d("m","Elapsed time: " + getElapsedTime().getValue() + " minutes,");
            }
            Log.d("s","Elapsed time: " + elapsedSeconds  + " seconds");
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void stop() {
        scheduler.shutdown();
    }
}
