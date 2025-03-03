package edu.ucsd.cse110.habitizer.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineTimer;
import edu.ucsd.cse110.habitizer.lib.domain.SimpleTask;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.Repository;
import edu.ucsd.cse110.habitizer.lib.domain.TaskList;
import edu.ucsd.cse110.habitizer.lib.util.Subject;
import android.util.Log;

public class MainViewModel extends ViewModel{
    private static final String LOG_TAG = "MainViewModel";
    private static final Integer ONE_MINUTE = 60;
    private final Repository repository;
    private final Subject<List<Task>> orderedTasks;
    private final Subject<String> routineTitle;
    private final Subject<Boolean> hasStarted;
    private final Subject<RoutineTimer> timer;
    private final Subject<Integer> elapsedTime;
    private final Subject<Integer> routineId;
    private final Subject<String> taskName;
    private final Subject<String> goalTime;
    private final Subject<List<Routine>> routines;

    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app =  (HabitizerApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getTaskRepository());
                    });

    public MainViewModel(Repository repository){
        this.repository = repository;

        this.orderedTasks = new Subject<>();
        this.routineId = new Subject<>();

        this.routineTitle = new Subject<>();
        this.hasStarted = new Subject<>();
        this.timer = new Subject<>();
        this.elapsedTime = new Subject<>();
        this.taskName = new Subject<>();
        this.goalTime = new Subject<>();
        this.routines = new Subject<>();

        this.routineId.setValue(0);

        repository.findAll().observe(list -> {
            Log.d("MVM obs routines", String.valueOf(repository.count()));
            routines.setValue(list);
        });
        //repository.findAll().observe(routines::setValue);

        routineId.observe(id -> {
            if(id == null)return;
            repository.find(id).observe(curRoutine -> {
                if(curRoutine == null)return;

                routineTitle.setValue(curRoutine.getName());
                hasStarted.setValue(curRoutine.getHasStarted());
                elapsedTime.setValue(curRoutine.getElapsedMinutes());
                goalTime.setValue(String.valueOf(curRoutine.getGoalTime()));
                orderedTasks.setValue(curRoutine.getTasks());
                timer.setValue(curRoutine.getTimer());
                //Log.d("MVM obs", routineTitle.getValue() + " hasStarted " + hasStarted.getValue() + " elp time " + elapsedTime.getValue() + " goal time " + goalTime.getValue());

                //when timers elapsedTime updates, update this elapsedTime
                if(timer.getValue() == null)return;
                timer.getValue().getElapsedMinutes().observe(val -> {
                    if(val == null) return;
                    //Log.d("MVM timer", "time received: " + val);
                    elapsedTime.setValue(val);

                });
            });

        });

        // When the ordering changes, update the first task
        //again might be useful
//        orderedTasks.observe(tasks -> {
//            if (tasks == null || tasks.size() == 0) return;
//            var task = tasks.get(0);
//            this.topTask.setValue(task);
//        });

    }

    public void newRoutine(){
        repository.save(new Routine());
        Log.d("MVM newRoutine", "has added");

    }

    public void startRoutine(){

        if (hasStarted.getValue() != null) return;
        var routine = repository.find(routineId.getValue()).getValue();
        if(routine == null) return;
        routine.setHasStarted(true);
        if (timer.getValue() == null) {
            routine.setTimer(new RoutineTimer(60));
        }
        repository.save(routine);
        timer.getValue().start();
    }

    public void endRoutine() {
        if (hasStarted.getValue() == null) return;
        if (!hasStarted.getValue()) return;
        hasStarted.setValue(false);
        //Log.d("MVM", "has started " + hasStarted.getValue());

        if (timer.getValue() == null) return;
        timer.getValue().end();
        var routine = repository.find(routineId.getValue()).getValue();
        if(routine == null) return;
        routine.setHasStarted(false);
        routine.setElapsedMinutes(timer.getValue().getElapsedMinutes().getValue());
        routine.setElapsedSeconds(timer.getValue().getElapsedSeconds());
        //Log.d("end routine", "has started " + hasStarted.getValue() + " mins " + routine.getElapsedMinutes() + " sec " + routine.getElapsedSeconds());

        repository.save(routine);
    }

    public void stopTimer() {
        var started = hasStarted.getValue();
        if (started == null) return;
        if (started) {
            timer.getValue().stop();
        }
        var routine = repository.find(routineId.getValue()).getValue();
        if(routine == null) return;
        routine.setElapsedMinutes(timer.getValue().getElapsedMinutes().getValue());
        routine.setElapsedSeconds(timer.getValue().getElapsedSeconds());
        //Log.d("stop routine", "has started " + hasStarted.getValue() + " mins " + routine.getElapsedMinutes() + " sec " + routine.getElapsedSeconds());
        repository.save(routine);
    }
    public void advanceTime() {
        if (timer.getValue() == null) return;
        timer.getValue().advanceTime(15);
    }
    public void swapRoutine() {
        //Log.d("MVM swap before", routineTitle.getValue() + " hasStarted " + hasStarted.getValue() );
        if (routineId.getValue() == null) return;
        if (routineId.getValue() == 0){
            routineId.setValue(1);
        } else {
            routineId.setValue(0);
        }
        //Log.d("MVM swap after", routineTitle.getValue() + " hasStarted " + hasStarted.getValue() );
    }

    public void addTask(Task task){
        if(orderedTasks.getValue() == null) return;
        if(routineId.getValue() == null) return;
        var newList = TaskList.addTask(orderedTasks.getValue(),new SimpleTask(task.getId(), task.getTaskName(),task.getRoutineId()));
        var newRoutine = repository.find(routineId.getValue()).getValue();
        if(newRoutine == null) return;
        newRoutine.setTasks(newList);
        repository.save(newRoutine);
    }

    public void setTaskName(int taskId, String taskName){
        var newList = TaskList.editTaskName(orderedTasks.getValue(),taskId,taskName);
        var newRoutine = repository.find(routineId.getValue()).getValue();
        if(newRoutine == null) return;
        newRoutine.setTasks(newList);
        repository.save(newRoutine);
    }
    public void setGoalTime(String goalTime){
        var newRoutine = repository.find(routineId.getValue()).getValue();
        if(newRoutine == null) return;
        newRoutine.setGoalTime(goalTime);
        repository.save(newRoutine);
    }

    public void setRoutineId(Integer id){
        routineId.setValue(id);
        Log.d("MVM setID","routine id = " + id);
    }

    public Subject<String> getTaskName(){
        return this.taskName;
    }


    public Subject<String> getGoalTime(){
        return this.goalTime;
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


    public Subject<List<Routine>> getRoutines() {
        return routines;
    }


    public Subject<Integer> getRoutineId() {
        return routineId;
    }

    //for testing
    public Repository getRepository() {
        return repository;
    }
}
