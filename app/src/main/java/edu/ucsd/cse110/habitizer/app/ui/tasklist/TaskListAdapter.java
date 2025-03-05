package edu.ucsd.cse110.habitizer.app.ui.tasklist;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static androidx.core.content.ContextCompat.getString;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.TaskItemBinding;
import edu.ucsd.cse110.habitizer.app.ui.dialog.EditTaskDialogFragment;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

public class TaskListAdapter extends ArrayAdapter<Task>{
    private int lastCheckedOffTime;
    private final MainViewModel mainViewModel;

    Consumer<Integer> onEditClick;

    public TaskListAdapter(Context context, List<Task> tasks, MainViewModel mainViewModel, Consumer<Integer> onEditClick){
        super(context, 0, new ArrayList<>(tasks));
        this.mainViewModel = mainViewModel;
        this.onEditClick = onEditClick;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        var task = getItem(position);
        assert task != null;

        TaskItemBinding binding;
        if(convertView != null){
            binding = TaskItemBinding.bind(convertView);
        }
        else{
            var layoutInflater = LayoutInflater.from(getContext());
            binding = TaskItemBinding.inflate(layoutInflater, parent, false);
        }

        binding.taskName.setText(task.getTaskName());
        binding.checkBox.setChecked(task.getCheckedOffStatus());
        binding.checkBox.setEnabled(!task.getCheckedOffStatus());   //enable checkbox after set

        if (!task.getCheckedOffStatus()) {
            binding.taskTime.setText("--");
        } else {
            binding.taskTime.setText(task.getCheckedOffTime() + " mins");
            //Log.d("tasklist ADAPTER", task.getCheckedOffTime());
        }

        boolean hasStarted = Boolean.TRUE.equals(mainViewModel.getHasStarted().getValue());
        binding.checkBox.setEnabled(hasStarted && !task.getCheckedOffStatus());

        binding.checkBox.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            if (!hasStarted) {
                buttonView.setChecked(false); // Prevent checking if not started
                return;
            }

            //Log.d("TaskListAdapter", "Task: " + task.getTaskName() + " Before: " + task.getCheckedOffStatus());

            if (isChecked) {
                int currentTime = mainViewModel.getElapsedTime().getValue();
                int timeTaken = 0;
                //task.setCheckedOff(true, (int)currentTime);
                binding.checkBox.setEnabled(false); //disable it so we cannot check it off

                timeTaken = currentTime - (int)lastCheckedOffTime + 1;
                task.setCheckedOff(true, (int)timeTaken);

                Log.d("TaskListAdapter", "Task: " + task.getTaskName() + " time: " + task.getCheckedOffTime());

                binding.taskTime.setText(timeTaken + " mins");
                lastCheckedOffTime = currentTime;

                checkAllTasksCheckedOff();
            }

            //Log.d("TaskListAdapter", "Task: " + task.getTaskName() + " After: " + task.getCheckedOffStatus());
        });

        binding.taskName.setOnClickListener(v -> {
            if(!hasStarted){
                var id = task.getId();
                assert id != null;
                onEditClick.accept(id);
            }
        });

        mainViewModel.getTaskName().observe(taskName -> {
            if(taskName != null){
                task.setName(taskName);
            }
        });
        return binding.getRoot();
    }

    public void checkAllTasksCheckedOff() {
        boolean allChecked = true;
        for (int i = 0; i < getCount(); i++) {
            if (!getItem(i).getCheckedOffStatus()) {
                allChecked = false;
                break;
            }
        }

        if (allChecked) {
            mainViewModel.endRoutine();
            Log.d("adap", "all checked off");
        }
    }

    @Override
    public boolean hasStableIds(){
        return true;
    }

    @Override
    public long getItemId(int position){
        var task = getItem(position);
        assert task != null;

        var id = task.getId();
        assert id != null;

        return id;
    }
}
