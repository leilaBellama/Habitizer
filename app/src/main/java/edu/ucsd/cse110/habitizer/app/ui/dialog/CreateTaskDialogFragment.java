package edu.ucsd.cse110.habitizer.app.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentDialogCreateTaskBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

public class CreateTaskDialogFragment extends DialogFragment{
    private FragmentDialogCreateTaskBinding view;
    private MainViewModel activityModel;
    CreateTaskDialogFragment(){

    }

    public static CreateTaskDialogFragment newInstance(){
        var fragment = new CreateTaskDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentDialogCreateTaskBinding.inflate(getLayoutInflater());

        return new AlertDialog.Builder(getActivity())
                .setTitle("New Task")
                .setMessage("Please name the new Task.")
                .setView(view.getRoot())
                .setPositiveButton("Add", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
    }

    private void onPositiveButtonClick(DialogInterface dialog, int which) {
        var taskName = view.createTaskNameText.getText().toString();

        var task = new Task(null, taskName, true);

        if (view.morningRadioButton.isChecked()) {
            task.setMorningTask(true);
        }
        else if (view.eveningRadioButton.isChecked()) {
            task.setMorningTask(false);
        } else {
            throw new IllegalStateException("No radio button is checked.");
        }

        activityModel.addTask(task);
        dialog.dismiss();
    }

    private void onNegativeButtonClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

    }
}
