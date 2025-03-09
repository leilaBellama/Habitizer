package edu.ucsd.cse110.habitizer.app.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentRoutinesPageBinding;
import edu.ucsd.cse110.habitizer.app.ui.dialog.CreateTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.dialog.EditGoalTimeDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.dialog.EditRoutineDialogFragment;

public class RoutinesPageFragment extends Fragment {
    private FragmentRoutinesPageBinding view;
    private MainViewModel model;
    private MenuProvider menuProvider;

    private boolean started = false;

    public RoutinesPageFragment() {
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
    public void onDestroyView(){
        super.onDestroyView();
        model.stopTimer();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = FragmentRoutinesPageBinding.inflate(inflater,container,false);
        setupMVP();
        return view.getRoot();
    }

    public void swapFragments() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.Home_page_fragment_container, new HomePageFragment())
                .commit();
    }

    private void setupMVP() {
        model.getRoutineTitle().observe(text -> view.routine.setText(text));
        model.getHasStarted().observe(hasStarted -> {
            if(hasStarted == null) reset();
            else if(hasStarted) startRoutine();
            else endRoutine();

        });
        model.getGoalTime().observe(goalTime -> {
            if(goalTime != null){
                if(!goalTime.equals("null")){
                    view.goalTime.setText(goalTime + " min");
                    Log.d("MA","obs goal time " + goalTime);
                }else {
                    Log.d("MA","obs goal time is null");
                    view.goalTime.setText(R.string.dashes);
                }
            }
        });
        model.getElapsedTime().observe(getViewLifecycleOwner(),time -> {
            if (time != null) {
                Log.d("MA obs timer","obs time " + time);
                view.time.setText(time + " min");
            } else {
                view.time.setText(R.string.dashes);
                Log.d("MA obs timer","obs null time ");
            }
        });
        view.homeButton.setOnClickListener(v -> swapFragments());
        view.startButton.setOnClickListener(v -> startRoutine());
        view.endButton.setOnClickListener(v -> endRoutine());
        view.resetButton.setOnClickListener(v -> reset());
        view.stopTime.setOnClickListener(v -> {
            model.stopTimer();
            view.stopTime.setVisibility(View.GONE);
            view.advanceTimeButton.setVisibility(View.VISIBLE);
        });
        view.advanceTimeButton.setOnClickListener(v -> model.advanceTime());
        view.addTaskButton.setOnClickListener(v -> {
            var dialogFragment = CreateTaskDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "CreateTaskDialogFragment");
        });
        view.goalTime.setOnClickListener(v -> {
            var dialogFragment = EditGoalTimeDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "EditGoalTimeDialogFragment");
        });

        //For changing routine name at the routine page
        view.routine.setOnClickListener(v -> {
            var dialogFragment = EditRoutineDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "EditRoutineDialogFragment");
        });
    }
    private void endRoutine() {
        view.advanceTimeButton.setVisibility(View.INVISIBLE);
        view.startButton.setVisibility(View.INVISIBLE);
        view.stopTime.setVisibility(View.INVISIBLE);
        view.endButton.setText("Routine Ended");
        view.endButton.setEnabled(false);
        view.endButton.requestLayout();
        view.resetButton.setVisibility(View.VISIBLE);
        view.homeButton.setVisibility(View.VISIBLE);
        model.endRoutine();
    }

    private void startRoutine() {
        view.startButton.setVisibility(View.INVISIBLE);
        view.endButton.setText("End");
        view.endButton.setVisibility(View.VISIBLE);
        view.endButton.setEnabled(true);
        view.addTaskButton.setVisibility(View.INVISIBLE);
        view.stopTime.setVisibility(View.VISIBLE);
        view.advanceTimeButton.setVisibility(View.VISIBLE);
        view.homeButton.setVisibility(View.INVISIBLE);
        model.startRoutine();
    }
    private void reset() {
        view.startButton.setVisibility(View.VISIBLE);
        //view.startButton.setEnabled(true);
        view.endButton.setVisibility(View.INVISIBLE);
        view.addTaskButton.setVisibility(View.VISIBLE);
        //view.addTaskButton.setEnabled(true);
        view.stopTime.setVisibility(View.INVISIBLE);
        view.advanceTimeButton.setVisibility(View.INVISIBLE);
        view.homeButton.setVisibility(View.VISIBLE);
        view.resetButton.setVisibility(View.INVISIBLE);
        model.reset();
    }
}