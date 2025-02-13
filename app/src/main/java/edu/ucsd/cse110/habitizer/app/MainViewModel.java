package edu.ucsd.cse110.habitizer.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;
import edu.ucsd.cse110.habitizer.lib.util.Subject;

import android.database.Observable;
import android.util.Log;

public class MainViewModel extends ViewModel{
    private static final String LOG_TAG = "MainViewModel";
    private static final Integer ONE_MINUTE = 60000;

    private final TaskRepository taskRepository;
    private final Subject<List<Integer>> taskOrderingEvening;
    private final Subject<List<Task>> orderedTasksEvening;
    private final Subject<List<Task>> orderedTasks;
    private final Subject<String> routineTitle;
    private final Subject<Boolean> hasStarted;
    private final Subject<RoutineTimer> timer;
    private final Subject<Integer> elapsedTime;
    private final Subject<Boolean> inMorning;
    private final Subject<List<Integer>> taskOrdering;
    private final Subject<List<Task>> orderedTasksMorning;

    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app =  (HabitizerApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getTaskRepository());
                    });

    public MainViewModel(TaskRepository taskRepository){
        this.taskRepository = taskRepository;

        this.orderedTasks = new Subject<>();

        this.taskOrdering = new Subject<>();
        this.orderedTasksMorning = new Subject<>();
        this.taskOrderingEvening = new Subject<>();
        this.orderedTasksEvening = new Subject<>();

        this.routineTitle = new Subject<>();
        this.inMorning = new Subject<>();
        this.hasStarted = new Subject<>();
        this.timer = new Subject<>();
        this.elapsedTime = new Subject<>();

        this.inMorning.setValue(true);
        this.hasStarted.setValue(false);
        this.elapsedTime.setValue(0);
        this.timer.setValue(new RoutineTimer());

        //when list changes (or is first loaded), reset ordering of both lists
        taskRepository.findAll().observe(tasks -> {
            if(tasks == null)   return;

            var ordering = new ArrayList<Integer>();
            for(int i = 0;i < tasks.size(); i++){
                ordering.add(i);
            }
            Log.d("obs", "observe TR");

            taskOrdering.setValue(ordering);
        });

        //when  list ordering changes, update both taskOrdering
        //might be useful later if we need to change order of tasks
        taskOrdering.observe(ordering -> {
            if(ordering == null) return;

            var morningTasks = new ArrayList<Task>();
            var eveningTasks = new ArrayList<Task>();
            for(var id : ordering){
                var task = taskRepository.find(id).getValue();
                if(task == null) return;
                if(task.isMorningTask()) {
                    morningTasks.add(task);
                } else {
                    eveningTasks.add(task);
                }
            }
            this.orderedTasksMorning.setValue(morningTasks);
            this.orderedTasksEvening.setValue(eveningTasks);
        });

        //when inMorning changes, switch title and morning/evening list
        inMorning.observe(inMorning -> {
            if (inMorning == null) return;
            if (!inMorning) {
                routineTitle.setValue("Evening Routine");
                orderedTasksEvening.observe(eveningTasks -> {
                    orderedTasks.setValue(orderedTasksEvening.getValue());

                });
            } else {
                routineTitle.setValue("Morning Routine");
                orderedTasksMorning.observe(eveningTasks -> {
                    orderedTasks.setValue(orderedTasksMorning.getValue());

                });
            }
        });

        // When the ordering changes, update the first task
        //again might be useful
//        orderedTasks.observe(tasks -> {
//            if (tasks == null || tasks.size() == 0) return;
//            var task = tasks.get(0);
//            this.topTask.setValue(task);
//        });

        this.timer.observe(routineTimer -> {
            if (routineTimer == null) return;

//            Timer t = routineTimer.getTimer();
//
//            if (t != null) {
//                t.cancel();
//                t.purge();
//            }
            Log.d("timer observe", "time" + elapsedTime.getValue());
            routineTimer.getElapsedTime().observe(elapsedTime::setValue);
        });

    }

    public void switchToMockTime() {
        /*
        if (hasStarted.getValue()) {
            timer.getValue().stop();
        }

         */
    }

    public Subject<String> getRoutineTitle(){
        return routineTitle;
    }

    public Subject<List<Task>> getOrderedTasks() {
        return orderedTasks;
    }

    public Subject<Integer> getElapsedTime() {
        return elapsedTime;
    }

    public Subject<Boolean> getHasStarted() {
        return hasStarted;
    }

    public void startRoutine(){
        var started = hasStarted.getValue();
        if (started == null) return;
        elapsedTime.setValue(0);
        if (!started) {
            hasStarted.setValue(true);
            timer.getValue().start();
            Log.d("ST", "started time" + elapsedTime.getValue());
        }
    }

    public void stopTimer() {
        var started = hasStarted.getValue();
        if (started == null) return;
        if (started) {
            timer.getValue().stop();
        }
    }
    public void swapRoutine() {
        var isMorning = this.inMorning.getValue();
        if (isMorning == null) return;
        this.inMorning.setValue(!isMorning);
    }

    public void advanceTime() {
        timer.getValue().advanceTime(30);
    }
    //TODO let it receive custom tasks
    public void addTask(){
        var isMorning = this.inMorning.getValue();
        if (isMorning == null) return;
        if (isMorning) {
            Task newTask = new Task(null, "new test task m",true);
            taskRepository.save(newTask);
            Log.d("Add Task m", "Task added m");
        }
        else {
            Task newTask = new Task(null, "new test task",false);
            taskRepository.save(newTask);
            Log.d("Add Task", "Task added");
        }

    }

}
