package edu.ucsd.cse110.habitizer.app.ui.tasklist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentTaskListBinding;
import edu.ucsd.cse110.habitizer.app.ui.dialog.EditTaskDialogFragment;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.util.Observer;

/**
 * Fragment to see list of Routine's Tasks
 */
public class TaskListFragment extends Fragment{
    private MainViewModel activityModel;
    private FragmentTaskListBinding view;
    private TaskListAdapter adapter;
    Observer<List<Task>> orderedTaskObserver = tasks -> {
        if(tasks == null) return;
        adapter.notifyDataSetChanged();
    };
    Observer<Boolean> hasStartedObserver = started -> {
        if (started == null) return;
        adapter.notifyDataSetChanged();
    };

    private List<Task> taskList = new ArrayList<>();

    public TaskListFragment() {

    }

    public static TaskListFragment newInstance() {
        TaskListFragment fragment = new TaskListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var ModelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = ModelProvider.get(MainViewModel.class);

        this.adapter = new TaskListAdapter(taskList, activityModel, id ->{
            var dialogFragment = EditTaskDialogFragment.newInstance(id);
            dialogFragment.show(getParentFragmentManager(), "EditTaskDialogFragment");
        });
        activityModel.getOrderedTasks().observe(tasks -> {
            if(tasks == null) return;
            taskList.clear();
            taskList.addAll(new ArrayList<>(tasks));
            adapter.notifyDataSetChanged();
        });

        activityModel.getHasStarted().observe(started -> {
            if (started == null) return;
            adapter.notifyDataSetChanged();
        });


    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ){
        this.view = FragmentTaskListBinding.inflate(inflater, container, false);
        RecyclerView recyclerView = view.taskList;
        recyclerView.setLayoutManager(new NonScrollableLM(getContext()));
        recyclerView.setAdapter(adapter);
        //Enable drag and drop to reorder items
        //if routine is not started
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TaskItemTouchHelper());
        activityModel.getHasStarted().observe(started ->{
            if(Boolean.TRUE.equals(started)){
                itemTouchHelper.attachToRecyclerView(null);
            }
            else{
                itemTouchHelper.attachToRecyclerView(recyclerView);
            }
        });




        return view.getRoot();
    }

    private class TaskItemTouchHelper extends ItemTouchHelper.SimpleCallback {
        public TaskItemTouchHelper(){
            super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0);
        }

        //Detect Drag movement
        @Override
        public boolean onMove(RecyclerView recyclerView,
                              RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target){

            int fromPos = viewHolder.getAdapterPosition();
            int toPos = target.getAdapterPosition();

            Collections.swap(taskList, fromPos, toPos);
            for(int i = 0;i < taskList.size();i ++){
                Task task = taskList.get(i);
                task.setPosition(i+1);
            }
            adapter.notifyItemMoved(fromPos, toPos);
            return true;
        }

        //We dont need swipe here
        //but we have to override it so it's empty
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction){

        }

        @Override
        public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder){
            super.clearView(recyclerView, viewHolder);
            activityModel.updateTaskOrder(new ArrayList<>(taskList));
        }
    }

    //Want to disable scrolling for the routine
    public class NonScrollableLM extends LinearLayoutManager {
        public NonScrollableLM(Context context){
            super(context);
        }

        @Override
        public boolean canScrollVertically(){
            return false;
        }

        @Override
        public boolean canScrollHorizontally(){
            return false;
        }
    }


    @Override
    public void onDestroyView(){
        super.onDestroyView();
        activityModel.getOrderedTasks().removeObserver(orderedTaskObserver);
        activityModel.getHasStarted().removeObserver(hasStartedObserver);
    }
}
