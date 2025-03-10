package edu.ucsd.cse110.habitizer.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    /*
    private final MutableSubject<List<Task>> orderedTasks;
    private final MutableSubject<List<Integer>> taskOrdering;
    private final MutableSubject<String> routineTitle;
    private final MutableSubject<Boolean> hasStarted;
    private final MutableSubject<RoutineTimer> timer;
    private final MutableLiveData<Integer> elapsedTime;
    private final MutableSubject<Integer> routineId;
    private final MutableSubject<String> taskName;
    private final MutableSubject<String> goalTime;
    private final MutableSubject<List<Routine>> routines;
    private final MutableSubject<Routine> currentRoutine;

     */

    private MutableSubject<List<Task>> orderedTasks;
    private MutableSubject<List<Integer>> taskOrdering;
    private MutableSubject<String> routineTitle;
    private MutableSubject<Boolean> hasStarted;
    private MutableSubject<RoutineTimer> timer;
    private MutableLiveData<Integer> elapsedTime;
    private MutableSubject<Integer> routineId;
    private MutableSubject<String> taskName;
    private MutableSubject<String> goalTime;
    private MutableSubject<List<Routine>> routines;
    private MutableSubject<List<Task>> tasks;
    private MutableSubject<Routine> currentRoutine;
    private MutableSubject<String> routineName;

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
        this.elapsedTime = new MutableLiveData<>();
        this.taskName = new SimpleSubject<>();
        this.goalTime = new SimpleSubject<>();
        this.routines = new SimpleSubject<>();
        this.tasks = new SimpleSubject<>();
        this.routineName = new SimpleSubject<>();
        this.timer.setValue(new RoutineTimer(60));

        repository.findAllRoutines().observe(routines::setValue);
        repository.findAllTasks().observe(tasks::setValue);

        currentRoutine.observe(curRoutine -> {
            if(curRoutine == null)return;
            //Log.d("MVM obs tasks", "has observers " +repository.findAllTasks().hasObservers());
            routineTitle.setValue(curRoutine.getName());
            hasStarted.setValue(curRoutine.getHasStarted());
            Log.d("MVM obs CR","obs curRoutine " + curRoutine.getId() + hasStarted.getValue());
            elapsedTime.setValue(curRoutine.getElapsedMinutes());
            goalTime.setValue(String.valueOf(curRoutine.getGoalTime()));

        });

        routineId.observe(id -> {
            if(id == null)return;
            currentRoutine.setValue(repository.getRoutine(id));
            Log.d("MVM obs id",id + " routine has obs " + repository.findRoutine(id).hasObservers());
            if(tasks == null) return;
            var newOrderedTasks = tasks.getValue().stream()
                    .filter(task -> Objects.equals(task.getRoutineId(), routineId.getValue()))
                    .toList();
            orderedTasks.setValue(newOrderedTasks);
        });

        timer.getValue().getElapsedMinutes().observe(time -> {
            if(time == null){
                Log.d("MVM obs timer null", "null time");
            }else{
                //Log.d("MVM obs timer", time.toString());
                elapsedTime.postValue(time);
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
        //if(routineId.getValue() == null || hasStarted.getValue() == null) return;
        if(routineId.getValue() == null) return;
        var resetList = TaskList.resetAll(orderedTasks.getValue());

        orderedTasks.setValue(resetList);
        //repository.saveTasks(resetList);
        //Log.d("MVM reset","reset tasks");
        //var routine = repository.findRoutine(routineId.getValue()).getValue();
        //var routine = repository.getRoutine(routineId.getValue());
        var routine = currentRoutine.getValue();
        if(routine == null) return;
        if(routine.getHasStarted() == null) return;
        routine.setHasStarted(null);
        routine.setElapsedMinutes(null);
        routine.setElapsedSeconds(null);
        currentRoutine.setValue(routine);
        //repository.saveRoutine(routine);
        Log.d("MVM reset", "cur routine id " + routineId.getValue() + currentRoutine.hasObservers());
        //save();

    }

    //has started must be null
    public void startRoutine(){
        if (timer.getValue() == null) return;
        if(hasStarted.getValue() != null) return;
        //var routine = repository.findRoutine(routineId.getValue()).getValue();
        //var routine = repository.getRoutine(routineId.getValue());
        var routine = currentRoutine.getValue();
        //Log.d("MVM start", "cur routine id " + routineId.getValue());

        if(routine == null) return;
        if(routine.getHasStarted() != null) return;
        routine.setElapsedMinutes(0);
        //Log.d("MVM start set","set mins");
        routine.setHasStarted(true);
        //Log.d("MVM start set","set has started true");
        Log.d("MVM start", "cur routine id " + routineId.getValue());
        currentRoutine.setValue(routine);
        //repository.saveRoutine(routine);
        Log.d("MVM start", "cur routine id " + routineId.getValue());
        timer.getValue().start();
    }

    //has started must be true
    public void endRoutine() {
        Log.d("MVM end","has started " + hasStarted.getValue());

        if (hasStarted.getValue() == null || !hasStarted.getValue()) return;
        if (timer.getValue() == null) return;
        timer.getValue().end();

        //var routine = repository.findRoutine(routineId.getValue()).getValue();
        //var routine = repository.getRoutine(routineId.getValue());
        var routine = currentRoutine.getValue();
        if(routine == null) return;
        if(routine.getHasStarted() == null || !routine.getHasStarted()) return;
        routine.setHasStarted(false);
        //Log.d("MVM end set","set has started");
        routine.setElapsedMinutes(timer.getValue().getElapsedMinutes().getValue());
        //Log.d("MVM end set","set elp min");
        routine.setElapsedSeconds(timer.getValue().getElapsedSeconds());
        //Log.d("MVM end set","set elp sec");
        currentRoutine.setValue(routine);
        //repository.saveRoutine(routine);
        //Log.d("MVM ended","has started " + hasStarted.getValue());

        //Log.d("MVM end","ending");
        repository.saveTasks(orderedTasks.getValue());
        if(orderedTasks.getValue() == null)return;
        for(int i = 0; i < orderedTasks.getValue().size(); i++){
            //Log.d("MVM end tasks", orderedTasks.getValue().get(i).getName());
        }
    }

    public void stopTimer() {
        if(timer.getValue() == null) return;
        timer.getValue().stop();

        Log.d("MVM stoptimer","stopping");
    }
    public void advanceTime() {
        if (timer.getValue() == null) return;
        timer.getValue().advanceTime(15);
    }

    public void addTask(Task task){
        if(task == null || orderedTasks.getValue() == null || routineId.getValue() == null) return;
        task.setRoutineId(routineId.getValue());
        //var newTasks = List.copyOf(orderedTasks.getValue());
        //newTasks.add(task);
        var tasks = new ArrayList<Task>();
        for(var taskItem : orderedTasks.getValue()){
            if(taskItem == null) return;
            tasks.add(taskItem);
        }
        tasks.add(task);
        orderedTasks.setValue(tasks);
        //repository.saveTask(task);
    }

    public void setTaskName(int taskId, String taskName){
        if(orderedTasks.getValue() == null) return;
        Task task = repository.getTask(taskId);
        if(task == null) return;
        task.setName(taskName);
        var tasks = new ArrayList<Task>();
        for(var taskItem : orderedTasks.getValue()){
            if(taskItem == null) return;
            if(taskItem.getId() != null && taskItem.getId() == taskId) tasks.add(task);
            else tasks.add(taskItem);
        }
        orderedTasks.setValue(tasks);
        //task.setName(taskName);
        //repository.saveTask(task);
    }
    public void setGoalTime(String goalTime){
        //var routine = repository.findRoutine(routineId.getValue()).getValue();
        var routine = repository.getRoutine(routineId.getValue());
        //var routine = currentRoutine.getValue();
        if(routine == null) return;
        routine.setGoalTime(goalTime);
        currentRoutine.setValue(routine);
        //repository.saveRoutine(routine);
    }

    public void setRoutineId(Integer id){

        Log.d("MVM set id","after routine " + id);
        routineId.setValue(id);
    }

    public void setRoutineName(String name){
        //var routine = repository.findRoutine(routineId.getValue()).getValue();
        var routine = repository.getRoutine(routineId.getValue());
        //var routine = currentRoutine.getValue();
        if(routine == null) return;
        routine.setName(name);
        currentRoutine.setValue(routine);
        //repository.saveRoutine(routine);
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

    //for testing
    public Subject<Integer> getRoutineId() {return routineId;}

    public Repository getRepository() {
        return repository;
    }

    public Subject<String> getRoutineName(){
        return routineName;
    }
}
