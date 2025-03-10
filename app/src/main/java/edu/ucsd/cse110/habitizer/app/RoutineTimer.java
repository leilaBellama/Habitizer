package edu.ucsd.cse110.habitizer.app;

import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import edu.ucsd.cse110.habitizer.lib.util.MutableSubject;
import edu.ucsd.cse110.habitizer.lib.util.SimpleSubject;

public class RoutineTimer {
    private ScheduledExecutorService scheduler;
    private int elapsedSeconds;
    private MutableSubject<Boolean> hasStarted;
    private MutableSubject<Integer> elapsedMin;

    private Integer interval;

    public RoutineTimer(Integer interval) {
        this.hasStarted = new SimpleSubject<>();
        this.elapsedMin = new SimpleSubject<>();
        //this.elapsedMin.setValue(0);
        //Log.d("timer", "initializing elapsedMin");

        this.interval = interval;

        //hasStarted.setValue(false);
    }

    public RoutineTimer(Integer interval,ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
        this.hasStarted = new SimpleSubject<>();
        this.elapsedMin = new SimpleSubject<>();
        this.interval = interval;
        //this.elapsedMin.setValue(0);
        this.elapsedSeconds = 0;
        //this.hasStarted.setValue(false);
    }

    public MutableSubject<Integer> getElapsedMinutes() {
        return elapsedMin;
    }

    public int getElapsedSeconds() {
        return elapsedSeconds;
    }

    public MutableSubject<Boolean> getHasStarted() {
        return hasStarted;
    }

    public void setTime(Integer minutes, int seconds) {
        this.getElapsedMinutes().setValue(minutes);
        this.elapsedSeconds = seconds;
    }

    public void advanceTime(Integer advance) {
        //if (!hasStarted.getValue()) return;
        elapsedSeconds += advance;
        if (elapsedSeconds >= interval) {
            elapsedMin.setValue(elapsedMin.getValue()+1);
            elapsedSeconds -= interval;
        }
        //Log.d("m","Advanced by 15 seconds minutes: " + getElapsedMinutes().getValue() + " seconds " + elapsedSeconds);
    }

    public void start() {
        if (scheduler == null || scheduler.isShutdown()) { // Create a new instance if shutdown
            scheduler = Executors.newScheduledThreadPool(1);
        }
        elapsedMin.setValue(0);
        //Log.d("timer", "setting time 0");
        elapsedSeconds = 0;
        scheduler.scheduleWithFixedDelay(() -> {
            elapsedSeconds++;
            if (elapsedSeconds >= interval) {
                elapsedMin.setValue(elapsedMin.getValue() + 1);

                elapsedSeconds = 0;
                //Log.d("m","Elapsed time: " + getElapsedMinutes().getValue() + " minutes,");
            }
            //Log.d("s", elapsedSeconds  + " seconds");
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void stop() {
        if(scheduler != null && !scheduler.isShutdown()){
            scheduler.shutdown();
        }
        //Log.d("m", "Timer stopped at: " + getElapsedMinutes().getValue() + " minutes");
    }

    public void end() {
        stop();
        int roundUp = 0;
        if (elapsedSeconds > 0) { roundUp = 1;}
        if(elapsedMin.getValue() == null)return;
        elapsedMin.setValue(elapsedMin.getValue() + roundUp);
        //Log.d("m","Ended at: " + getElapsedMinutes().getValue() + " minutes");
    }
}
