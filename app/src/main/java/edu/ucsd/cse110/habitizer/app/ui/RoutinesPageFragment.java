package edu.ucsd.cse110.habitizer.app.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentRoutinesPageBinding;
import edu.ucsd.cse110.habitizer.app.ui.dialog.CreateTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.dialog.EditGoalTimeDialogFragment;

public class RoutinesPageFragment extends Fragment {
    private FragmentRoutinesPageBinding view;
    private MainViewModel model;


    public RoutinesPageFragment() {
        // Required empty public constructor
    }

    public static RoutinesPageFragment newInstance() {
        RoutinesPageFragment fragment = new RoutinesPageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.model = modelProvider.get(MainViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = FragmentRoutinesPageBinding.inflate(inflater,container,false);
        setupMVP();
        return view.getRoot();
    }

    private void setupMVP() {
        model.getRoutineTitle().observe(text -> view.routine.setText(text));
        model.getHasStarted().observe(hasStarted -> {
            if (hasStarted == null) reset();
            else if (!hasStarted) endRoutine();
            else startRoutine();

        });
        model.getGoalTime().observe(goalTime -> {
            if(goalTime != null){
                view.goalTime.setText(goalTime + " min");
            }
        });
        model.getElapsedTime().observe(time -> {
            if (time != null) {
                view.time.setText(time + " min");
                //runOnUiThread(() -> view.time.setText(time + " min"));
                Log.d("MA","obs time " + time);
            }
        });
        view.startButton.setOnClickListener(v -> startRoutine());
        view.endButton.setOnClickListener(v -> endRoutine());
        view.stopTime.setOnClickListener(v -> {
            model.stopTimer();
            view.stopTime.setVisibility(View.GONE);
        });
        view.advanceTimeButton.setOnClickListener(v -> model.advanceTime());
        view.addTaskButton.setOnClickListener(v -> {
            //if(started){
                //view.addTaskButton.setEnabled(false);
            //}else {
                var dialogFragment = CreateTaskDialogFragment.newInstance();
                dialogFragment.show(getParentFragmentManager(), "CreateTaskDialogFragment");
            //}
        });
        view.goalTime.setOnClickListener(v -> {
            var dialogFragment = EditGoalTimeDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "EditGoalTimeDialogFragment");
        });
    }
    private void endRoutine() {
        view.advanceTimeButton.setVisibility(View.GONE);
        view.stopTime.setVisibility(View.GONE);
        view.startButton.setVisibility(View.GONE);
        view.endButton.setText("Routine Ended");
        view.endButton.setVisibility(View.VISIBLE);
        view.endButton.setEnabled(false);
        view.endButton.requestLayout();
        model.endRoutine();
        //started = false;
        //invalidateOptionsMenu();
    }

    private void startRoutine() {
        view.startButton.setVisibility(View.GONE);
        view.endButton.setText("End");
        view.endButton.setVisibility(View.VISIBLE);
        view.endButton.setEnabled(true);
        view.addTaskButton.setVisibility(View.GONE);
        view.stopTime.setVisibility(View.VISIBLE);
        view.advanceTimeButton.setVisibility(View.VISIBLE);
        model.startRoutine();
        //started = true;
        //invalidateOptionsMenu();
    }
    private void reset() {
        view.startButton.setVisibility(View.VISIBLE);
        view.startButton.setEnabled(true);
        view.endButton.setVisibility(View.GONE);
        view.addTaskButton.setVisibility(View.VISIBLE);
        view.addTaskButton.setEnabled(true);
        view.stopTime.setVisibility(View.GONE);
        view.advanceTimeButton.setVisibility(View.GONE);
        //started = false;
        //invalidateOptionsMenu();
    }
}