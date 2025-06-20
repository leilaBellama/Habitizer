package edu.ucsd.cse110.habitizer.app.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentEditTaskDialogBinding;

/**
 * Fragment to edit task name
 */
public class EditTaskDialogFragment extends DialogFragment {

    private static final String ARG_TASK_ID = "task_id";
    private int taskId;
    private FragmentEditTaskDialogBinding view;
    private MainViewModel activityModel;

    //empty Constructor
    public EditTaskDialogFragment(){};

    public static EditTaskDialogFragment newInstance(int taskId){
        var fragment = new EditTaskDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TASK_ID, taskId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        this.taskId = requireArguments().getInt(ARG_TASK_ID);
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentEditTaskDialogBinding.inflate(getLayoutInflater());

        return new AlertDialog.Builder(getActivity())
                .setTitle("Edit Task Name")
                .setMessage("Please enter the new task name")
                .setView(view.getRoot())
                .setPositiveButton("Confirm", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .setNeutralButton("Delete", this::onDeleteButtonClick)
                .create();
    }

    private void onPositiveButtonClick(DialogInterface dialog, int which) {
        var taskName = view.editTaskName.getText().toString();

        if (taskName == null || taskName.isEmpty()) {
            Toast.makeText(requireContext(), "Task name cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        activityModel.setTaskName(taskId, taskName);
        dialog.dismiss();
    }

    private void onNegativeButtonClick(DialogInterface dialog, int which){
        dialog.cancel();
    }

    //New neutral button on the left bottom corner of the dialog
    private void onDeleteButtonClick(DialogInterface dialog, int which){
        activityModel.removeTask(taskId);
        dialog.dismiss();
    }
}
