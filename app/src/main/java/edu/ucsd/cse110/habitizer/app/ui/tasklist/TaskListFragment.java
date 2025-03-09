package edu.ucsd.cse110.habitizer.app.ui.tasklist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentTaskListBinding;
import edu.ucsd.cse110.habitizer.app.ui.dialog.EditTaskDialogFragment;
//import edu.ucsd.cse110.habitizer.app.ui.dialog.CreateTaskDialogFragment;

public class TaskListFragment extends Fragment{
    private MainViewModel activityModel;
    private FragmentTaskListBinding view;
    private TaskListAdapter adapter;

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

        this.adapter = new TaskListAdapter(requireContext(), List.of(), activityModel, id ->{
            var dialogFragment = EditTaskDialogFragment.newInstance(id);
            dialogFragment.show(getParentFragmentManager(), "EditTaskDialogFragment");
        });
        activityModel.getOrderedTasks().observe(tasks -> {
            if(tasks == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(tasks));
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
        view.taskList.setAdapter(adapter);
        return view.getRoot();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        activityModel.getOrderedTasks().removeAllObservers();
        activityModel.getHasStarted().removeAllObservers();
    }
}
