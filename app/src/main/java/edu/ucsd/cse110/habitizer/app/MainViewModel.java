package edu.ucsd.cse110.habitizer.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.MutableCreationExtras;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.SimpleTask;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.Repository;
import edu.ucsd.cse110.habitizer.lib.domain.TaskList;
import edu.ucsd.cse110.habitizer.lib.util.Subject;

public class MainViewModel extends ViewModel{
    private static final String LOG_TAG = "MainViewModel";
    private static final Integer ONE_MINUTE = 60;
    private final Repository repository;
    private final Subject<List<Task>> orderedTasks;
    private final Subject<String> routineTitle;
    private final Subject<Boolean> hasStarted;
    private final Subject<RoutineTimer> timer;
    private final MutableLiveData<Integer> elapsedTime;
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
        this.elapsedTime = new MutableLiveData<>();
        this.taskName = new Subject<>();
        this.goalTime = new Subject<>();
        this.routines = new Subject<>();

        this.timer.setValue(new RoutineTimer(60));

        repository.findAll().observe(routines::setValue);

        routineId.observe(id -> {
            if(id == null)return;
            repository.find(id).observe(curRoutine -> {
                if(curRoutine == null)return;

                routineTitle.setValue(curRoutine.getName());
                hasStarted.setValue(curRoutine.getHasStarted());
                elapsedTime.setValue(curRoutine.getElapsedMinutes());
                goalTime.setValue(String.valueOf(curRoutine.getGoalTime()));
                orderedTasks.setValue(curRoutine.getTasks());

            });

        });

        timer.getValue().getElapsedMinutes().observe(val -> {
            if(val == null) return;
            elapsedTime.postValue(val);
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
    }


    public void reset(){

        if(routineId.getValue() == null) return;
        var routine = repository.find(routineId.getValue()).getValue();
        if(routine == null) return;

        /*
        routine.setHasStarted(null);
        routine.setElapsedSeconds(null);
        routine.setElapsedMinutes(null);
        routine.setTasks(TaskList.resetAll(routine.getTasks()));

         */
        //repository.save(routine);/////LINE CAUSES ISSUE

        /*
        var list = TaskList.resetAll(routine.getTasks());
        var newRoutine = new RoutineBuilder()
                .setId(routine.getId())
                .setName(routine.getName())
                .setTasks(list)
                .setGoalTime(routine.getGoalTime())
                .buildRoutine();

         */

    }

    public void startRoutine(){
        if (hasStarted.getValue() != null) return;

        var routine = repository.find(routineId.getValue()).getValue();
        if(routine == null) return;
        routine.setHasStarted(true);
        repository.save(routine);
        timer.getValue().start();
    }

    public void endRoutine() {
        if (hasStarted.getValue() == null) return;
        if (!hasStarted.getValue()) return;
        hasStarted.setValue(false);

        if (timer.getValue() == null) return;
        timer.getValue().end();
        var routine = repository.find(routineId.getValue()).getValue();
        if(routine == null) return;
        routine.setHasStarted(false);
        routine.setElapsedMinutes(timer.getValue().getElapsedMinutes().getValue());
        routine.setElapsedSeconds(timer.getValue().getElapsedSeconds());
        repository.save(routine);
    }

    public void stopTimer() {
        var started = hasStarted.getValue();
        if (started == null) return;
        if (!started) return;
        if(timer.getValue() == null) return;
        timer.getValue().stop();

        var routine = repository.find(routineId.getValue()).getValue();
        if(routine == null) return;
        routine.setElapsedMinutes(timer.getValue().getElapsedMinutes().getValue());
        routine.setElapsedSeconds(timer.getValue().getElapsedSeconds());
        repository.save(routine);
    }
    public void advanceTime() {
        if (timer.getValue() == null) return;
        timer.getValue().advanceTime(15);
        var routine = repository.find(routineId.getValue()).getValue();
        if(routine == null) return;
        routine.setElapsedMinutes(timer.getValue().getElapsedMinutes().getValue());
        routine.setElapsedSeconds(timer.getValue().getElapsedSeconds());

    }

    public void addTask(Task task){
        if(routineId.getValue() == null) return;
        var tasks = orderedTasks.getValue();
        if(tasks == null) tasks = new ArrayList<>();
        var newList = TaskList.addTask(tasks,new SimpleTask(task.getId(), task.getTaskName()));
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

    public MutableLiveData<Integer> getElapsedTime() {
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
