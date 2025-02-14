package edu.ucsd.cse110.habitizer.app.ui.tasklist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.ArrayList;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.TaskItemBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

public class TaskListAdapter extends ArrayAdapter<Task>{
    private Double lastCheckedOffTime = null;
    private final MainViewModel mainViewModel;

    public TaskListAdapter(Context context, List<Task> tasks, MainViewModel mainViewModel){
        super(context, 0, new ArrayList<>(tasks));
        this.mainViewModel = mainViewModel;
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
        }

        boolean hasStarted = Boolean.TRUE.equals(mainViewModel.getHasStarted().getValue());
        binding.checkBox.setEnabled(hasStarted && !task.getCheckedOffStatus());

        binding.checkBox.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            if (!hasStarted) {
                buttonView.setChecked(false); // Prevent checking if not started
                return;
            }

            Log.d("TaskListAdapter", "Task: " + task.getTaskName() + " Before: " + task.getCheckedOffStatus());

            if (isChecked) {
                double currentTime = mainViewModel.getElapsedTime().getValue();
                task.setCheckedOff(true, (int)currentTime);
                binding.checkBox.setEnabled(false); //disable it so we cannot check it off

                if (lastCheckedOffTime != null) {
                    double timeTaken = currentTime - lastCheckedOffTime;
                    Log.d("TaskListAdapter", "Time taken since last task: " + timeTaken + " mins");
                }

                int timeTaken;
                if (lastCheckedOffTime == null) {
                    timeTaken = (int) currentTime;
                } else {
                    timeTaken = (int) currentTime - lastCheckedOffTime.intValue();
                }

                binding.taskTime.setText(timeTaken + " mins");
                lastCheckedOffTime = currentTime;
            }

            Log.d("TaskListAdapter", "Task: " + task.getTaskName() + " After: " + task.getCheckedOffStatus());
        });
        return binding.getRoot();
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
