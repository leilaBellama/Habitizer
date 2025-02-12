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
    private final TaskRepository taskRepository;
    private final Subject<List<Integer>> taskOrderingMorning;
    private final Subject<List<Task>> orderedTasksMorning;
    private final Subject<List<Integer>> taskOrderingEvening;
    private final Subject<List<Task>> orderedTasksEvening;
    private final Subject<List<Task>> orderedTasks;
    private final Subject<String> routineTitle;
    private final Subject<Boolean> hasStarted;
    private final Subject<Integer> elapsedTime;
    private final Subject<Boolean> inMorning;
    private Timer timer;

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

        this.taskOrderingMorning = new Subject<>();
        this.orderedTasksMorning = new Subject<>();
        this.taskOrderingEvening = new Subject<>();
        this.orderedTasksEvening = new Subject<>();

        this.routineTitle = new Subject<>();
        this.inMorning = new Subject<>();
        this.hasStarted = new Subject<>();
        this.elapsedTime = new Subject<>();

        inMorning.setValue(true);
        hasStarted.setValue(false);
        elapsedTime.setValue(0);

        //when morning list changes (or is first loaded), reset ordering of both lists
        taskRepository.getBoth().observe(tasks -> {
            if (tasks == null || tasks.isEmpty()) return;
            if (tasks.get(0) != null) {
                var ordering = new ArrayList<Integer>();
                for(int i = 0; i < Objects.requireNonNull(tasks.get(0).getValue()).size(); i++){
                    ordering.add(i);
                }
                taskOrderingMorning.setValue(new ArrayList<>(ordering));
            }
            if (tasks.get(1) != null) {
                var ordering = new ArrayList<Integer>();
                for(int i = 0;i < Objects.requireNonNull(tasks.get(1).getValue()).size(); i++){
                    ordering.add(i);
                }
                taskOrderingEvening.setValue(new ArrayList<>(ordering));
            }

        });

        //when morning list ordering changes, update morning taskOrdering
        //might be useful later if we need to change order of tasks
        taskOrderingMorning.observe(ordering -> {
            if(ordering == null) return;
            var tasks = new ArrayList<Task>();
            for(var id : ordering){
                var task = taskRepository.findMorning(id).getValue();
                if(task == null) return;
                tasks.add(task);
            }
            this.orderedTasksMorning.setValue(new ArrayList<Task>(tasks));
        });

        //when evening list ordering changes, update evening taskOrdering
        taskOrderingEvening.observe(ordering -> {
            //Log.d("DEBUG", "task order evening emitted: " + (ordering != null ? ordering.size() : "null"));
            if(ordering == null) return;
            var tasks = new ArrayList<Task>();
            for(var id : ordering){
                var task = taskRepository.findEvening(id).getValue();
                if(task == null) return;
                tasks.add(task);
            }
            this.orderedTasksEvening.setValue(new ArrayList<Task>(tasks));
        });

        //when inMorning changes, switch title and morning/evening list
        inMorning.observe(inMorning -> {
            if (inMorning == null) return;
            if (!inMorning) {
                routineTitle.setValue("Evening Routine");
                orderedTasks.setValue(orderedTasksEvening.getValue());
            } else {
                routineTitle.setValue("Morning Routine");
                orderedTasks.setValue(orderedTasksMorning.getValue());
            }
        });

        // When the ordering changes, update the first task
        //again might be useful
//        orderedTasks.observe(tasks -> {
//            if (tasks == null || tasks.size() == 0) return;
//            var task = tasks.get(0);
//            this.topTask.setValue(task);
//        });

        //when hasStarted changes, start timer
        hasStarted.observe(hasStarted -> {
            if (hasStarted == null || !hasStarted) return;

            if (timer != null) {
                timer.cancel();
                timer.purge();
            }

            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Log.d("timer", "time " + elapsedTime.getValue());
                    var time = elapsedTime.getValue();
                    if(time == null) return;
                    elapsedTime.setValue((int) (time + 1.0));
                }
            };
            timer.schedule(task,0,60000);//60000 milliseconds = 1 minute
        });
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
        var started = this.hasStarted.getValue();
        if (started == null || started) return;
        this.hasStarted.setValue(true);

        this.elapsedTime.setValue(0);
    }

    public void swapRoutine() {
        var isMorning = this.inMorning.getValue();
        if (isMorning == null) return;
        this.inMorning.setValue(!isMorning);
    }

    //TODO let it receive custom tasks
    public void addTask(){
        /*
        var isMorning = this.inMorning.getValue();
        if (isMorning == null) return;
        if (isMorning) {
            Task newTask = new Task(4, "new test task m");
            taskRepository.appendMorning(newTask);
            Log.d("Add Task m", "Task added m");
        }
        else {
            Task newTask = new Task(4, "new test task");
            taskRepository.appendEvening(newTask);
            Log.d("Add Task", "Task added");
        }

         */


    }

}
