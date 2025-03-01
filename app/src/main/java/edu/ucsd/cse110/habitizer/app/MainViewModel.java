package edu.ucsd.cse110.habitizer.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;
import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.lib.domain.SimpleTask;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;
import edu.ucsd.cse110.habitizer.lib.util.Subject;
import android.util.Log;

public class MainViewModel extends ViewModel{

    private static final String LOG_TAG = "MainViewModel";
    private static final Integer ONE_MINUTE = 60;
    private final TaskRepository taskRepository;
    private final Subject<List<Task>> allOrderedTasks;
    private final Subject<List<Task>> orderedTasks;
    private final Subject<String> routineTitle;
    private final Subject<Boolean> hasStarted;
    //private final Subject<Boolean> routineEnded;
    private final Subject<RoutineTimer> timer;
    private final Subject<Integer> elapsedTime;
    private final Subject<Integer> routineId;
    //private final Subject<Boolean> inMorning;
    private final Subject<List<Integer>> taskOrdering;
    //private final Subject<List<Task>> orderedTasksMorning;
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
        this.routineId = new Subject<>();

        this.taskOrdering = new Subject<>();
        this.allOrderedTasks = new Subject<>();
        //this.orderedTasksEvening = new Subject<>();

        this.routineTitle = new Subject<>();
        //this.inMorning = new Subject<>();
        this.hasStarted = new Subject<>();
        this.timer = new Subject<>();
        this.elapsedTime = new Subject<>();
        this.taskName = new Subject<>();
        //this.routineEnded = new Subject<>();
        this.goalTime = new Subject<>();


        this.routineId.setValue(0);
        //this.hasStarted.setValue(false);
        this.elapsedTime.setValue(0);
        this.timer.setValue(new RoutineTimer(ONE_MINUTE));
        //this.routineEnded.setValue(false);

        //when list changes (or is first loaded), reset ordering of list
        taskRepository.findAllTasks().observe(tasks -> {
            if(tasks == null)   return;

            var ordering = new ArrayList<Integer>();
            for(int i = 0;i < tasks.size(); i++){
                ordering.add(i);
                //Log.d("obs", "ordering " + i);
            }

            taskOrdering.setValue(ordering);
        });

        //when  list ordering changes, update both taskOrdering
        //might be useful later if we need to change order of tasks
        taskOrdering.observe(ordering -> {
            if(ordering == null) return;
            var tasks = new ArrayList<Task>();
            for(var id : ordering){
                var task = taskRepository.findTask(id).getValue();
                //Log.d("id", "id " + id);

                if(task == null) return;
                //Log.d("obs", "task name " + task.getTaskName());
                tasks.add(task);
            }
            this.allOrderedTasks.setValue(tasks);
            /*

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

             */
        });

        allOrderedTasks.observe(tasks -> {
            if(tasks == null)return;
            var taskList = new ArrayList<Task>();

            for(var task : tasks){
                if(task == null) return;
                if(task.getRoutineId() == routineId.getValue()){
                    taskList.add(task);
                }
            }
            orderedTasks.setValue(taskList);
        });

        taskRepository.findRoutine(routineId.getValue()).observe(curRoutine -> {
            routineTitle.setValue(curRoutine.getName());
            hasStarted.setValue(curRoutine.getHasStarted());
            elapsedTime.setValue(curRoutine.getElapsedMinutes());
            goalTime.setValue(String.valueOf(curRoutine.getGoalTime()));
            Log.d("obs findRoutine", "title " + routineTitle.getValue() + " hasStarted " + hasStarted.getValue() + " elp time " + elapsedTime.getValue() + " goal time " + goalTime.getValue());

        });

        routineId.observe(id -> {
            if (id == null) return;
            var tasks = new ArrayList<Task>();
            if (allOrderedTasks.getValue() == null) return;
            for(var task : allOrderedTasks.getValue()){
                if(task == null) return;
                if(task.getRoutineId() == id){
                    tasks.add(task);
                }
            }
            orderedTasks.setValue(tasks);
            var curRoutine = taskRepository.findRoutine(id).getValue();
            routineTitle.setValue(curRoutine.getName());
            hasStarted.setValue(curRoutine.getHasStarted());
            elapsedTime.setValue(curRoutine.getElapsedMinutes());
            goalTime.setValue(String.valueOf(curRoutine.getGoalTime()));
            Log.d("obs routineId", "title " + routineTitle.getValue() + " hasStarted " + hasStarted.getValue() + " elp time " + elapsedTime.getValue() + " goal time " + goalTime.getValue());

        });

        //when timers elapsedTime updates, update this elapsedTime
        timer.getValue().getElapsedMinutes().observe(val -> {
            if(val == null) return;
            Log.d("timer", "time received: " + val);
            elapsedTime.setValue(val);

            /*
            Integer currentTime = elapsedTime.getValue();
            if (currentTime == null) currentTime = 0;
            if (!currentTime.equals(val)) {
                elapsedTime.setValue(currentTime + 1);
                //Log.d("timer", "after setValue");
            }

             */
        });
        /*

        //when inMorning changes, switch title and morning/evening list
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

        // When the ordering changes, update the first task
        //again might be useful
//        orderedTasks.observe(tasks -> {
//            if (tasks == null || tasks.size() == 0) return;
//            var task = tasks.get(0);
//            this.topTask.setValue(task);
//        });



         */

    }

    public void startRoutine(){
        // set start button as disabled (use if needed)
//        if (goalTime.getValue() == null || goalTime.getValue().isEmpty()) {
//            //Log.d(LOG_TAG, "Cannot start routine. Goal time is not set.");
//            return; // Do not start if goal time is not set
//        }
        Log.d("s", "started");
        if (timer.getValue() == null) return;
        if (hasStarted.getValue() != null) return;
        var routine = taskRepository.findRoutine(routineId.getValue()).getValue();
        routine.setHasStarted(true);
        taskRepository.saveRoutine(routine);
        timer.getValue().start();
        /*
        hasStarted.setValue(true);
        Log.d("s", "has started " + hasStarted.getValue());

        if (hasStarted.getValue()) {
            Log.d("s", "has started " + hasStarted.getValue());

            timer.getValue().start();
        }

         */
    }

    public void endRoutine() {
        if (hasStarted.getValue() == null) return;
        if (!hasStarted.getValue()) return;


        //Log.d("end routine", "has started " + hasStarted.getValue());

        if (timer.getValue() == null) return;
        timer.getValue().end();

        var routine = taskRepository.findRoutine(routineId.getValue()).getValue();
        routine.setHasStarted(false);
        routine.setElapsedMinutes(timer.getValue().getElapsedMinutes().getValue());
        routine.setElapsedSeconds(timer.getValue().getElapsedSeconds());
        Log.d("end routine", "has started " + hasStarted.getValue() + " mins " + routine.getElapsedMinutes() + " sec " + routine.getElapsedSeconds());

        taskRepository.saveRoutine(routine);
    }

    public void stopTimer() {
        var started = hasStarted.getValue();
        if (started == null) return;
        if (started) {
            timer.getValue().stop();
        }
        var routine = taskRepository.findRoutine(routineId.getValue()).getValue();
        routine.setElapsedMinutes(timer.getValue().getElapsedMinutes().getValue());
        routine.setElapsedSeconds(timer.getValue().getElapsedSeconds());
        Log.d("stop routine", "has started " + hasStarted.getValue() + " mins " + routine.getElapsedMinutes() + " sec " + routine.getElapsedSeconds());

        taskRepository.saveRoutine(routine);
    }
    public void advanceTime() {
        timer.getValue().advanceTime(30);
    }
    public void swapRoutine() {
        Log.d("swap", "swap routine " +routineId.getValue());
        if (routineId.getValue() == null) return;
        if (routineId.getValue() == 0){
            routineId.setValue(1);
        } else {
            routineId.setValue(0);
        }
        Log.d("swap", "swap routine " +routineId.getValue());

    }

    public void addTask(Task task){
        if(task == null){return;}
        var newTask = new SimpleTask(task.getId(), task.getTaskName(),task.getRoutineId());
        taskRepository.saveTask(newTask);
    }

    public void setTaskName(int taskId, String taskName){
        var task = taskRepository.findTask(taskId).getValue();
        task.setName(taskName);
        taskRepository.saveTask(task);
        //taskRepository.editTaskName(taskId, taskName);
    }
    public void setGoalTime(String goalTime){
        var routine = taskRepository.findRoutine(routineId.getValue()).getValue();
        routine.setGoalTime(goalTime);
        taskRepository.saveRoutine(routine);
        //this.goalTime.setValue(goalTime);
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



}
