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
import edu.ucsd.cse110.habitizer.app.databinding.FragmentEditGoalTimeDialogBinding;

/**
 * Fragment to edit Routine's goal time
 */
public class EditGoalTimeDialogFragment extends DialogFragment{
    private FragmentEditGoalTimeDialogBinding view;
    private MainViewModel activityModel;

    //need empty constructor
    public EditGoalTimeDialogFragment(){}

    public static EditGoalTimeDialogFragment newInstance() {
        var fragment = new EditGoalTimeDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentEditGoalTimeDialogBinding.inflate(getLayoutInflater());

        return new AlertDialog.Builder(getActivity())
                .setTitle("Edit Goal Time")
                .setMessage("Please enter the new goal time.")
                .setView(view.getRoot())
                .setPositiveButton("Confirm", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
                    v -> onPositiveButtonClick(dialog, 0));
        }
    }

    private void onPositiveButtonClick(DialogInterface dialog, int which){
        var goalTime = view.editGoalTimeText.getText().toString();

        if (goalTime.isEmpty()) {
            Toast.makeText(getActivity(), "Goal Time Cannot Be Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        try{
            int goalTimeInt = Integer.parseInt(goalTime);
            if (goalTimeInt < 1) {
                Toast.makeText(getContext(), "You must enter an integer from 1 and above.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            activityModel.setGoalTime(goalTime);
            dialog.dismiss();
        }
        catch(NumberFormatException e){
            Toast.makeText(getActivity(), "Goal Time Must Be An Integer",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void onNegativeButtonClick(DialogInterface dialog, int which){
        dialog.cancel();
    }

}
