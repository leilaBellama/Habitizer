package edu.ucsd.cse110.habitizer.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;
import java.util.ArrayList;
import java.util.List;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;
import edu.ucsd.cse110.habitizer.lib.util.Subject;
import android.util.Log;

public class MainViewModel extends ViewModel{
    private static final String LOG_TAG = "MainViewModel";
    private static final Integer ONE_MINUTE = 60;
    private final TaskRepository taskRepository;
    private final Subject<List<Task>> orderedTasksEvening;
    private final Subject<List<Task>> orderedTasks;
    private final Subject<String> routineTitle;
    private final Subject<Boolean> hasStarted;
    private final Subject<RoutineTimer> timer;
    private final Subject<Integer> elapsedTime;
    private final Subject<Boolean> inMorning;
    private final Subject<List<Integer>> taskOrdering;
    private final Subject<List<Task>> orderedTasksMorning;
    private final Subject<String> taskName;
    private final Subject<String> goalTime;

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
        this.orderedTasksEvening = new Subject<>();

        this.routineTitle = new Subject<>();
        this.inMorning = new Subject<>();
        this.hasStarted = new Subject<>();
        this.timer = new Subject<>();
        this.elapsedTime = new Subject<>();
        this.taskName = new Subject<>();
        this.goalTime = new Subject<>();

        this.inMorning.setValue(true);
        this.elapsedTime.setValue(0);
        this.timer.setValue(new RoutineTimer(ONE_MINUTE));

        taskRepository.findAll().observe(tasks -> {
            if(tasks == null)   return;

            var ordering = new ArrayList<Integer>();
            for(int i = 0;i < tasks.size(); i++){
                ordering.add(i);
            }
            taskOrdering.setValue(ordering);
        });

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

        inMorning.observe(inMorning -> {
            if (inMorning == null) return;
            if (!inMorning) {
                routineTitle.setValue("Evening Routine");
                orderedTasksMorning.removeObservers();
                orderedTasksEvening.observe(eveningTasks -> {
                    orderedTasks.setValue(orderedTasksEvening.getValue());
                });
            } else {
                routineTitle.setValue("Morning Routine");
                orderedTasksEvening.removeObservers();
                orderedTasksMorning.observe(eveningTasks -> {
                    orderedTasks.setValue(orderedTasksMorning.getValue());

                });
            }
        });

        timer.getValue().getElapsedTime().observe(val -> {
            Integer currentTime = elapsedTime.getValue();
            if (currentTime == null) currentTime = 0;
            if (!currentTime.equals(val)) {
                elapsedTime.setValue(val);
            }
        });
    }

    public void startRoutine(){
        Log.d("s", "started");
        if (timer.getValue() == null) return;
        if (hasStarted.getValue() != null) return;
        hasStarted.setValue(true);
        Log.d("s", "has started " + hasStarted.getValue());

        if (hasStarted.getValue()) {
            Log.d("s", "has started " + hasStarted.getValue());

            timer.getValue().start();
        }
    }

    public void endRoutine() {
        if (hasStarted.getValue() == null) return;
        if (!hasStarted.getValue()) return;
        hasStarted.setValue(false);
        Log.d("MVM", "has started " + hasStarted.getValue());

        if (timer.getValue() == null) return;
        timer.getValue().end();
    }

    public void stopTimer() {
        var started = hasStarted.getValue();
        if (started == null) return;
        if (started) {
            timer.getValue().stop();
        }
    }
    public void advanceTime() {
        timer.getValue().advanceTime(15);
    }
    public void swapRoutine() {
        var isMorning = this.inMorning.getValue();
        if (isMorning == null) return;
        this.inMorning.setValue(!isMorning);
    }

    public void addTask(Task task){
        if(task == null){return;}
        taskRepository.save(task);
    }

    public void setTaskName(int taskId, String taskName){
        taskRepository.editName(taskId, taskName);
    }

    public Subject<String> getTaskName(){
        return this.taskName;
    }

    public Subject<String> getGoalTime(){
        return this.goalTime;
    }
    public void setGoalTime(String goalTime){
        this.goalTime.setValue(goalTime);
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

}
