package edu.ucsd.cse110.habitizer.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;
import edu.ucsd.cse110.habitizer.lib.util.Subject;

public class MainViewModel extends ViewModel{
    private static final String LOG_TAG = "MainViewModel";

    private final TaskRepository taskRepository;

    private final Subject<List<Integer>> taskOrdering;
    private final Subject<List<Task>> orderedTasks;
    private final Subject<String> displayedText;

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



    }

    public Subject<String> getDisplayedText(){
        return displayedText;
    }

    public Subject<List<Task>> getOrderedTasks() {
        return orderedTasks;
    }


}
