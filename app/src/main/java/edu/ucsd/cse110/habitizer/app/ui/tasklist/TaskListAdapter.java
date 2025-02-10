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

import edu.ucsd.cse110.habitizer.app.databinding.TaskItemBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

public class TaskListAdapter extends ArrayAdapter<Task>{
    public TaskListAdapter(Context context, List<Task> tasks){
        super(context, 0, new ArrayList<>(tasks));
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

        binding.checkBox.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            Log.d("TaskListAdapter", "Task: " + task.getTaskName() + " Before: " + task.getCheckedOffStatus());

            if (isChecked) {
                task.setCheckedOff(true);
                binding.checkBox.setEnabled(false); //disable it so we cannot check it off
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
