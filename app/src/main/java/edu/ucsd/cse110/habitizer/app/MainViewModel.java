package edu.ucsd.cse110.habitizer.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;
import edu.ucsd.cse110.habitizer.lib.util.Subject;
import android.util.Log;

public class MainViewModel extends ViewModel{
    private static final String LOG_TAG = "MainViewModel";

    private final TaskRepository taskRepository;

    private final Subject<List<Integer>> taskOrdering;
    private final Subject<List<Task>> orderedTasks;
    private final Subject<String> displayedText;
    private final Subject<Boolean> hasStarted;
    private final Subject<Integer> elapsedTime;
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

        this.taskOrdering = new Subject<>();
        this.orderedTasks = new Subject<>();
        this.displayedText = new Subject<>();
        this.hasStarted = new Subject<>();
        this.elapsedTime = new Subject<>();

        hasStarted.setValue(false);

        taskRepository.findAll().observe(tasks -> {
            if(tasks == null)   return;

            var ordering = new ArrayList<Integer>();
            for(int i = 0;i < tasks.size(); i++){
                ordering.add(i);
            }

            taskOrdering.setValue(ordering);
        });

        //might be useful later if we need to change order of tasks
        taskOrdering.observe(ordering -> {
            if(ordering == null) return;

            var tasks = new ArrayList<Task>();
            for(var id : ordering){
                var task = taskRepository.find(id).getValue();
                if(task == null) return;

                tasks.add(task);
            }
            this.orderedTasks.setValue(tasks);
        });

        // When the ordering changes, update the first task
        //again might be useful
//        orderedTasks.observe(tasks -> {
//            if (tasks == null || tasks.size() == 0) return;
//            var task = tasks.get(0);
//            this.topTask.setValue(task);
//        });

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

    public Subject<String> getDisplayedText(){
        return displayedText;
    }

    public Subject<List<Task>> getOrderedTasks() {
        return orderedTasks;
    }
    public Subject<Integer> getElapsedTime() {
        return elapsedTime;
    }

    public void startRoutine(){
        var started = this.hasStarted.getValue();
        if (started == null) return;
        this.hasStarted.setValue(true);

        this.elapsedTime.setValue(0);

    }


}
