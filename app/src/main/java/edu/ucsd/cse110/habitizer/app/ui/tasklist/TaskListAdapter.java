package edu.ucsd.cse110.habitizer.app.ui.tasklist;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static androidx.core.content.ContextCompat.getString;

import android.content.Context;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.TaskItemBinding;
import edu.ucsd.cse110.habitizer.app.ui.dialog.EditTaskDialogFragment;
import edu.ucsd.cse110.habitizer.lib.domain.SimpleTask;
import edu.ucsd.cse110.habitizer.lib.domain.SimpleTaskBuilder;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

/**
 * Displays a Routines Tasks in a list format
 */
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {
    private final List<Task> taskList;
    private final MainViewModel mainViewModel;
    private final Consumer<Integer> onEditClick;
    private int lastCheckedOffTime = 0;
    private int lastCheckedOffSecs = 0;


    public TaskListAdapter(List<Task> taskList, MainViewModel mainViewModel, Consumer<Integer> onEditClick) {
        this.taskList = taskList;
        this.mainViewModel = mainViewModel;
        this.onEditClick = onEditClick;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        TaskItemBinding binding = TaskItemBinding.inflate(inflater, parent, false);
        return new TaskViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        private final TaskItemBinding binding;

        public TaskViewHolder(TaskItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Task task) {
            binding.taskName.setText(task.getName());
            binding.checkBox.setChecked(task.getCheckedOffStatus());
            binding.checkBox.setEnabled(!task.getCheckedOffStatus());

            if (!task.getCheckedOffStatus()) {
                binding.taskTime.setText("--");
            } else {
                binding.taskTime.setText(task.getCheckedOffTime());
            }

            boolean hasStarted = Boolean.TRUE.equals(mainViewModel.getHasStarted().getValue());
            binding.checkBox.setEnabled(hasStarted && !task.getCheckedOffStatus());

            if (!hasStarted) {
                lastCheckedOffSecs = 0;
                lastCheckedOffTime = 0;
            }

            binding.checkBox.setEnabled(hasStarted && !task.getCheckedOffStatus());

            binding.checkBox.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
                if (!hasStarted) {
                    return;
                }

                if (isChecked) {
                    if (mainViewModel.getElapsedTime() == null || mainViewModel.getElapsedTime().getValue() == null)
                        return;
                    int currentTime = mainViewModel.getElapsedTime().getValue();
                    int currentSecs = mainViewModel.getSecs();
                    String timeTaken;
                    int time = 0;
                    binding.checkBox.setEnabled(false); //disable it so we cannot check it off
                    mainViewModel.resetTaskTimer();

                    int diffTime = (60 * currentTime) - (60 * lastCheckedOffTime) + currentSecs - lastCheckedOffSecs;
                    if (diffTime < 60) {
                        time = ((diffTime / 5 * 5) + 5);
                        timeTaken = time + " secs";
                        // 60 secs roundup
                        if (time == 60) {
                            timeTaken = 1 + " mins";
                        }
                    } else {
                        time = (diffTime / 60) + 1;
                        timeTaken = time + " mins";
                    }

                    task.setCheckedOff(true);
                    task.setCheckedOffTime(timeTaken);

                    Log.d("TaskListAdapter", "Task: " + task.getName() + " time: " + task.getCheckedOffTime());

                    binding.taskTime.setText(timeTaken);
                    lastCheckedOffTime = currentTime;
                    lastCheckedOffSecs = currentSecs;

                    checkAllTasksCheckedOff();
                }
            });

            binding.taskName.setOnClickListener(v -> {
                if (!hasStarted) {
                    onEditClick.accept(task.getId());
                }
            });
        }

        public void checkAllTasksCheckedOff() {
            boolean allChecked = true;
            for (int i = 0; i < taskList.size(); i++) {
                if (!taskList.get(i).getCheckedOffStatus()) {
                    allChecked = false;
                    break;
                }
            }

            if (allChecked) {
                mainViewModel.endRoutine();
                Log.d("adap", "all checked off");
            }
        }
    }
}