package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.Timer;
import java.util.TimerTask;

import edu.ucsd.cse110.habitizer.lib.util.Subject;

public class RoutineTimer {
    private Timer timer;
    private Subject<Boolean> hasStarted;
    private Subject<Integer> elapsedTime;
    private Integer interval;
    private Integer mostRecentTime;


    public RoutineTimer(Integer interval) {
        this.hasStarted = new Subject<>();
        this.elapsedTime = new Subject<>();
        this.interval = interval;
        this.timer = new Timer();

        elapsedTime.setValue(0);
        hasStarted.setValue(false);
    }

    public Subject<Integer> getElapsedTime() {
        return elapsedTime;
    }

    public Subject<Boolean> getHasStarted() {
        return hasStarted;
    }
//
//    public void setTime(Integer time) {
//        this.elapsedTime = time;
//    }
//
//    public void advanceTime(Integer advance) {
//        this.elapsedTime = this.elapsedTime + advance;
//    }

    public void start() {
        if (this.hasStarted.getValue()) return;
        this.hasStarted.setValue(true);
        this.elapsedTime.setValue(0);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                elapsedTime.setValue(elapsedTime.getValue() + 1);
            }
        }, 0, interval);
    }
}
