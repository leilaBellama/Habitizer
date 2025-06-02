package edu.ucsd.cse110.habitizer.app;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import edu.ucsd.cse110.habitizer.lib.util.MutableSubject;
import edu.ucsd.cse110.habitizer.lib.util.SimpleSubject;

/**
 * Timer object to keep track of elapsed time
 */
public class RoutineTimer {
    private ScheduledExecutorService scheduler;
    private int elapsedSeconds;
    private MutableSubject<Boolean> hasStarted;
    private MutableSubject<Integer> elapsedMin;

    private Integer interval;

    public RoutineTimer(Integer interval) {
        this.hasStarted = new SimpleSubject<>();
        this.elapsedMin = new SimpleSubject<>();
        this.elapsedMin.setValue(0);
        this.interval = interval;
    }

    public RoutineTimer(Integer interval,ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
        this.hasStarted = new SimpleSubject<>();
        this.elapsedMin = new SimpleSubject<>();
        this.interval = interval;
        this.elapsedMin.setValue(0);
        this.elapsedSeconds = 0;
        this.hasStarted.setValue(false);
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
        elapsedSeconds += advance;
        if (elapsedSeconds >= interval) {
            elapsedMin.setValue(elapsedMin.getValue()+1);
            elapsedSeconds -= interval;
        }
    }

    public void start() {
        if (scheduler == null || scheduler.isShutdown()) { // Create a new instance if shutdown
            scheduler = Executors.newScheduledThreadPool(1);
        }
        elapsedMin.setValue(0);
        elapsedSeconds = 0;
        scheduler.scheduleWithFixedDelay(() -> {
            elapsedSeconds++;
            if (elapsedSeconds >= interval) {
                elapsedMin.setValue(elapsedMin.getValue() + 1);

                elapsedSeconds = 0;
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void stop() {
        if(scheduler != null && !scheduler.isShutdown()){
            scheduler.shutdown();
        }
    }

    public void end() {
        stop();
        if(elapsedMin.getValue() == null)return;
        elapsedMin.setValue(elapsedMin.getValue() + 1);
        elapsedSeconds = 0;
    }
}
