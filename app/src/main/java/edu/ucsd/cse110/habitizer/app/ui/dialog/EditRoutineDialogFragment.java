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
import edu.ucsd.cse110.habitizer.app.databinding.FragmentDialogEditRoutineBinding;



public class EditRoutineDialogFragment extends DialogFragment{

    private FragmentDialogEditRoutineBinding view;
    private MainViewModel activityModel;

    public EditRoutineDialogFragment(){}

    public static EditRoutineDialogFragment newInstance(){
        var fragment = new EditRoutineDialogFragment();
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
        this.view = FragmentDialogEditRoutineBinding.inflate(getLayoutInflater());

        return new AlertDialog.Builder(getActivity())
                .setTitle("Edit Routine Name")
                .setMessage("Please enter the new routine name")
                .setView(view.getRoot())
                .setPositiveButton("Confirm", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
    }

    private void onPositiveButtonClick(DialogInterface dialog, int which){
        var name = view.editRoutineName.getText().toString();

        if (name.isEmpty()) {
            Toast.makeText(getActivity(), "Routine Name Cannot Be Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        activityModel.setRoutineName(name);
        dialog.dismiss();
    }

    private void onNegativeButtonClick(DialogInterface dialog, int which){dialog.cancel();}
}
