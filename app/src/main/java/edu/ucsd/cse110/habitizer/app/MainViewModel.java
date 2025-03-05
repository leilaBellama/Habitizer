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
    private final Subject<List<Integer>> taskOrdering;
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
        this.taskOrdering = new Subject<>();
        this.routineId = new Subject<>();

        this.routineTitle = new Subject<>();
        this.hasStarted = new Subject<>();
        this.timer = new Subject<>();
        this.elapsedTime = new MutableLiveData<>();
        this.taskName = new Subject<>();
        this.goalTime = new Subject<>();
        this.routines = new Subject<>();

        this.timer.setValue(new RoutineTimer(60));

        repository.findAllTasks().observe(tasks -> {
            if(tasks == null) return;
            Log.d("MVM","obs tasks order");

            var ordering = new ArrayList<Integer>();
            for(Task task : tasks){
                if(task.getId() != null) {
                    ordering.add(task.getId());
                }
            }
            taskOrdering.setValue(ordering);

        });

        repository.findAllRoutines().observe(routines::setValue);

        routineId.observe(id -> {
            if(id == null)return;
            repository.findRoutines(id).observe(curRoutine -> {
                if(curRoutine == null)return;

                routineTitle.setValue(curRoutine.getName());
                hasStarted.setValue(curRoutine.getHasStarted());
                elapsedTime.setValue(curRoutine.getElapsedMinutes());
                goalTime.setValue(String.valueOf(curRoutine.getGoalTime()));
                taskOrdering.observe(ordering -> {
                    Log.d("MVM","obs tasks");

                    if(ordering == null) return;
                    var tasks = new ArrayList<Task>();
                    for(var taskId : ordering){
                        var task = repository.findTask(taskId).getValue();
                        if(task == null) return;
                        if(Objects.equals(task.getRoutineId(), id)) {
                            tasks.add(task);
                        }
                    }
                    this.orderedTasks.setValue(tasks);
                });

            });

        });

        timer.getValue().getElapsedMinutes().observe(val -> {
            //if(val == null) return;
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
        repository.saveRoutine(new Routine());
    }


    public void reset(){

        if(routineId.getValue() == null) return;
        var routine = repository.findRoutines(routineId.getValue()).getValue();
        if(routine == null) return;


        var resetList = TaskList.resetAll(orderedTasks.getValue());
        repository.saveTasks(resetList);
        routine.setHasStarted(null);
        routine.setElapsedSeconds(null);
        routine.setElapsedMinutes(null);
        //routine.setTasks(TaskList.resetAll(routine.getTasks()));
        routine.setHasStarted(null);

        repository.saveRoutine(routine);


        //repository.saveRoutine(routine);/////LINE CAUSES ISSUE

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

        var routine = repository.findRoutines(routineId.getValue()).getValue();
        if(routine == null) return;
        routine.setHasStarted(true);
        repository.saveRoutine(routine);
        timer.getValue().start();
    }

    public void endRoutine() {
        if (hasStarted.getValue() == null) return;
        if (!hasStarted.getValue()) return;
        hasStarted.setValue(false);

        if (timer.getValue() == null) return;
        timer.getValue().end();
        var routine = repository.findRoutines(routineId.getValue()).getValue();
        if(routine == null) return;
        routine.setHasStarted(false);
        routine.setElapsedMinutes(timer.getValue().getElapsedMinutes().getValue());
        routine.setElapsedSeconds(timer.getValue().getElapsedSeconds());
        repository.saveRoutine(routine);
    }

    public void stopTimer() {
        var started = hasStarted.getValue();
        if (started == null) return;
        if (!started) return;
        if(timer.getValue() == null) return;
        timer.getValue().stop();

        var routine = repository.findRoutines(routineId.getValue()).getValue();
        if(routine == null) return;
        routine.setElapsedMinutes(timer.getValue().getElapsedMinutes().getValue());
        routine.setElapsedSeconds(timer.getValue().getElapsedSeconds());
        repository.saveRoutine(routine);
    }
    public void advanceTime() {
        if (timer.getValue() == null) return;
        timer.getValue().advanceTime(15);
        var routine = repository.findRoutines(routineId.getValue()).getValue();
        if(routine == null) return;
        routine.setElapsedMinutes(timer.getValue().getElapsedMinutes().getValue());
        routine.setElapsedSeconds(timer.getValue().getElapsedSeconds());

    }

    public void addTask(Task task){
        var newTask = new SimpleTask(null,task.getTaskName(),routineId.getValue());
        repository.saveTask(newTask);
    }

    public void setTaskName(int taskId, String taskName){
        var newList = TaskList.editTaskName(orderedTasks.getValue(),taskId,taskName);
        repository.saveTasks(newList);
    }
    public void setGoalTime(String goalTime){
        var newRoutine = repository.findRoutines(routineId.getValue()).getValue();
        if(newRoutine == null) return;
        newRoutine.setGoalTime(goalTime);
        repository.saveRoutine(newRoutine);
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
