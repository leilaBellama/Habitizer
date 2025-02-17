package edu.ucsd.cse110.habitizer.app.ui.tasklist;

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
                task.setCheckedOff(true, (int)currentTime);
                binding.checkBox.setEnabled(false); //disable it so we cannot check it off

                /*
                if (lastCheckedOffTime != null) {
                    double timeTaken = currentTime - lastCheckedOffTime;
                    //Log.d("TaskListAdapter", "Task: " + task.getTaskName() + "Time taken since last task: " + timeTaken + " mins");
                }
                if (lastCheckedOffTime == null) {
                    timeTaken = (int) currentTime;
                } else {
                    timeTaken = (int) currentTime - lastCheckedOffTime.intValue();
                }

                 */
                timeTaken = currentTime - (int)lastCheckedOffTime + 1;

                Log.d("TaskListAdapter", "Task: " + task.getTaskName() + "cur time: " + currentTime + " mins" + "last time" + lastCheckedOffTime);


                binding.taskTime.setText(timeTaken + " mins");
                lastCheckedOffTime = currentTime;
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
