package edu.ucsd.cse110.habitizer.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineBuilder;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.Repository;
import edu.ucsd.cse110.habitizer.lib.domain.TaskList;
import edu.ucsd.cse110.habitizer.lib.util.MutableSubject;
import edu.ucsd.cse110.habitizer.lib.util.SimpleSubject;
import edu.ucsd.cse110.habitizer.lib.util.Subject;

public class MainViewModel extends ViewModel{
    private static final String LOG_TAG = "MainViewModel";
    private static final Integer ONE_MINUTE = 60;
    private final Repository repository;
    private final MutableSubject<List<Task>> orderedTasks;
    private final MutableSubject<List<Integer>> taskOrdering;
    private final MutableSubject<String> routineTitle;
    private final MutableSubject<Boolean> hasStarted;
    private final MutableSubject<RoutineTimer> timer;
    private final MutableSubject<RoutineTimer> taskTimer;
    private final MutableLiveData<Integer> elapsedTime;
    private final MutableLiveData<Integer> taskElapsedTime;
    private final MutableSubject<Integer> routineId;
    private final MutableSubject<String> taskName;
    private final MutableSubject<String> goalTime;
    private final MutableSubject<List<Routine>> routines;
    private final MutableSubject<Routine> currentRoutine;
    private final MutableSubject<List<Task>> tasks;
    private final MutableSubject<String> routineName;

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
        this.orderedTasks = new SimpleSubject<>();
        this.taskOrdering = new SimpleSubject<>();
        this.routineId = new SimpleSubject<>();
        this.currentRoutine = new SimpleSubject<>();

        this.routineTitle = new SimpleSubject<>();
        this.hasStarted = new SimpleSubject<>();
        this.timer = new SimpleSubject<>();
        this.taskTimer = new SimpleSubject<>();
        this.elapsedTime = new MutableLiveData<>();
        this.taskElapsedTime = new MutableLiveData<>();
        this.taskName = new SimpleSubject<>();
        this.goalTime = new SimpleSubject<>();
        this.routines = new SimpleSubject<>();
        this.tasks = new SimpleSubject<>();
        this.routineName = new SimpleSubject<>();
        this.timer.setValue(new RoutineTimer(60));
        this.taskTimer.setValue(new RoutineTimer(60));

        repository.findAllRoutines().observe(routines::setValue);
        repository.findAllTasks().observe(tasks::setValue);

        // When the current routine changes, update displays
        currentRoutine.observe(curRoutine -> {
            if(curRoutine == null)return;
            //Log.d("MVM obs tasks", "has observers " +repository.findAllTasks().hasObservers());
            routineTitle.setValue(curRoutine.getName());
            hasStarted.setValue(curRoutine.getHasStarted());
            Log.d("MVM obs CR","obs curRoutine " + curRoutine.getId() + hasStarted.getValue());
            elapsedTime.setValue(curRoutine.getElapsedMinutes());
            taskElapsedTime.setValue(curRoutine.getCurrentTaskTime());
            goalTime.setValue(String.valueOf(curRoutine.getGoalTime()));

        });

        // When a routine is selected, update current routine
        routineId.observe(id -> {
            if(id == null)return;
            currentRoutine.setValue(repository.getRoutine(id));
            Log.d("MVM obs id",id + " routine has obs " + repository.findRoutine(id).hasObservers());
            if(tasks.getValue() == null) return;
            var newOrderedTasks = tasks.getValue().stream()
                    .filter(task -> Objects.equals(task.getRoutineId(), routineId.getValue()))
                    .toList();
            orderedTasks.setValue(newOrderedTasks);
        });

        // Continue updating remaining time
        timer.getValue().getElapsedMinutes().observe(time -> {
            if(time == null){
                Log.d("MVM obs timer null", "null time");
            }else{
                //Log.d("MVM obs timer", time.toString());
                elapsedTime.postValue(time);
            }
        });

        // Update task time
        taskTimer.getValue().getElapsedMinutes().observe(time -> {
            if(time == null){
                Log.d("MVM obs timer null", "null time");
            }else{
                //Log.d("MVM obs timer", time.toString());
                taskElapsedTime.postValue(time);
            }
        });
    }

    public void save(){
        repository.saveRoutine(currentRoutine.getValue());
        repository.saveTasks(orderedTasks.getValue());
    }
    public void newRoutine(){
        var newRoutine = new RoutineBuilder().makeNewRoutine().buildRoutine();
        repository.saveRoutine(newRoutine);
    }

    //has started must be false
    public void reset(){
        if(routineId.getValue() == null) return;
        var resetList = TaskList.resetAll(orderedTasks.getValue());

        orderedTasks.setValue(resetList);
        var routine = currentRoutine.getValue();
        if(routine == null || routine.getHasStarted() == null) return;
        routine.setHasStarted(null);
        routine.setElapsedMinutes(null);
        routine.setElapsedSeconds(null);
        routine.setCurrentTaskTime(null);
        currentRoutine.setValue(routine);
        Log.d("MVM reset", "cur routine id " + routineId.getValue() + currentRoutine.hasObservers());
    }

    //has started must be null
    public void startRoutine(){
        if (timer.getValue() == null) return;
        if (taskTimer.getValue() == null) return;
        if(hasStarted.getValue() != null) return;
        var routine = currentRoutine.getValue();

        if(routine == null) return;
        if(routine.getHasStarted() != null) return;
        routine.setElapsedMinutes(0);
        routine.setHasStarted(true);
        Log.d("MVM start", "cur routine id " + routineId.getValue());
        currentRoutine.setValue(routine);
        Log.d("MVM start", "cur routine id " + routineId.getValue());
        timer.getValue().start();
        taskTimer.getValue().start();
    }

    //has started must be true
    public void endRoutine() {
        Log.d("MVM end","has started " + hasStarted.getValue());

        if (hasStarted.getValue() == null || !hasStarted.getValue()) return;
        if (timer.getValue() == null || taskTimer.getValue() == null) return;
        timer.getValue().end();
        taskTimer.getValue().end();
        var routine = currentRoutine.getValue();
        if(routine == null) return;
        if(routine.getHasStarted() == null || !routine.getHasStarted()) return;
        routine.setHasStarted(false);
        routine.setElapsedMinutes(timer.getValue().getElapsedMinutes().getValue());
        routine.setElapsedSeconds(timer.getValue().getElapsedSeconds());
        routine.setCurrentTaskTime(taskTimer.getValue().getElapsedMinutes().getValue());
        currentRoutine.setValue(routine);
        repository.saveTasks(orderedTasks.getValue());
        repository.saveRoutine(currentRoutine.getValue());
        if(orderedTasks.getValue() == null)return;
    }

    public void stopTimer() {
        if(timer.getValue() == null || taskTimer.getValue() == null) return;
        timer.getValue().stop();
        taskTimer.getValue().stop();
        Log.d("MVM stoptimer","stopping");
    }
    public void advanceTime() {
        if (timer.getValue() == null) return;
        timer.getValue().advanceTime(15);
        taskTimer.getValue().advanceTime(15);
    }

    public void addTask(Task task){
        if(task == null || orderedTasks.getValue() == null || routineId.getValue() == null) return;
        task.setRoutineId(routineId.getValue());
        task.setId(tasks.getValue().size() + 1);
        Log.d("MVM addTask", "task id " + task.getId() + " task routineId " + task.getRoutineId());
        var tasks =TaskList.addTask(orderedTasks.getValue(),task);
        orderedTasks.setValue(tasks);
        repository.saveTasks(tasks);
    }

    public void setTaskName(int taskId, String taskName){
        if(orderedTasks.getValue() == null) return;
        Task task = repository.getTask(taskId);
        if(task == null) return;
        var tasks = TaskList.editTaskName(orderedTasks.getValue(),taskId,taskName);
        orderedTasks.setValue(tasks);
        repository.saveTasks(tasks);
    }

    public void setGoalTime(String goalTime){
        var routine = repository.getRoutine(routineId.getValue());
        if(routine == null) return;
        routine.setGoalTime(goalTime);
        currentRoutine.setValue(routine);
        repository.saveRoutine(routine);
    }

    public void setRoutineId(Integer id){
        Log.d("MVM set id","after routine " + id);
        routineId.setValue(id);
    }

    public void setRoutineName(String name){
        if(routineId.getValue() == null) return;
        var routine = repository.getRoutine(routineId.getValue());
        if(routine == null) return;
        routine.setName(name);
        currentRoutine.setValue(routine);
        repository.saveRoutine(routine);
    }

    public Subject<Routine> getRoutine() {
        return currentRoutine;
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

    public MutableLiveData<Integer> getTaskElapsedTime() {
        return taskElapsedTime;
    }

    public Subject<Boolean> getHasStarted() {
        return hasStarted;
    }

    public Subject<List<Routine>> getRoutines() {
        return routines;
    }

    //for testing
    public Subject<Integer> getRoutineId() {return routineId;}

    public Repository getRepository() {
        return repository;
    }

    public Subject<String> getRoutineName(){
        return routineName;
    }

    // go through ids associated with routine
    // use temp variable, swap routine variables
    public void updateTaskOrder(List<Task> newTasks){
        // Update the in-memory LiveData/Subject
        orderedTasks.setValue(newTasks);

        var routine = currentRoutine.getValue();
        if(routine == null) return;

        routine.setTasks(newTasks);
        repository.saveTasks(newTasks);
    }

    public void removeTask(int taskId){
        if (routineId.getValue() == null) return;

        var tasks = orderedTasks.getValue();
        if (tasks == null) return;

        var newList = TaskList.removeTask(tasks, taskId);

        for(int i = 0;i < newList.size();i++){
            newList.get(i).setPosition(i+1);
        }
        repository.removeTask(taskId);
        orderedTasks.setValue(newList);
        repository.saveTasks(newList);
    }

    public void resetTaskTimer() {
        taskTimer.getValue().end();
        taskElapsedTime.setValue(0);
        taskTimer.getValue().start();
    }

    public int getSecs(){
        return this.timer.getValue().getElapsedSeconds();
    }
}
