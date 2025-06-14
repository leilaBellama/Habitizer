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
import edu.ucsd.cse110.habitizer.lib.util.Observer;

/**
 * Fragment for a Routine's page
 */
public class RoutinesPageFragment extends Fragment {
    private FragmentRoutinesPageBinding view;
    private MainViewModel model;
    private Boolean started;
    Observer<String> titleObserver = text -> view.routine.setText(text);
    Observer<Boolean> hasStartedObserver = hasStarted -> {
        if(hasStarted == started) return;
        if(hasStarted == null) reset();
        else if(hasStarted) start();
        else end();
    };

    Observer<String> goalTimeObserver = goalTime -> {
        if(goalTime == null)return;
        if(!goalTime.equals("null")){
            view.goalTime.setText(goalTime + " min");
        }else {
            view.goalTime.setText(R.string.dashes);
        }
    };

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
        model.save();

        model.getRoutineTitle().removeObserver(titleObserver);
        model.getHasStarted().removeObserver(hasStartedObserver);
        model.getGoalTime().removeObserver(goalTimeObserver);
        model.getElapsedTime().removeObservers(getViewLifecycleOwner());
        model.getTaskElapsedTime().removeObservers(getViewLifecycleOwner());
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
        model.getRoutineTitle().observe(titleObserver);
        model.getHasStarted().observe(hasStartedObserver);
        model.getGoalTime().observe(goalTimeObserver);
        model.getElapsedTime().observe(getViewLifecycleOwner(),time -> {
            if (time != null) {
                view.routineTime.setText(time + " out of");
            } else {
                view.routineTime.setText(R.string.dashes);
            }
        });
        model.getTaskElapsedTime().observe(getViewLifecycleOwner(),time -> {
            if (time != null) {
                view.taskTime.setText(time + " min");
            } else {
                view.taskTime.setText("---");
            }
        });
        view.homeButton.setOnClickListener(v -> {
            swapFragments();
        });
        view.startButton.setOnClickListener(v -> {
            model.startRoutine();
        });
        view.endButton.setOnClickListener(v -> {
            model.endRoutine();
        });
        view.resetButton.setOnClickListener(v -> {
            model.reset();
        });
        view.addTaskButton.setOnClickListener(v -> {
            var dialogFragment = CreateTaskDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "CreateTaskDialogFragment");
        });
        view.goalTime.setOnClickListener(v -> {
            var dialogFragment = EditGoalTimeDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "EditGoalTimeDialogFragment");
        });
        view.routine.setOnClickListener(v -> {
            var dialogFragment = EditRoutineDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "EditRoutineDialogFragment");
        });
        //test buttons
        /*
        view.stopTime.setOnClickListener(v -> {
            model.stopTimer();
            view.stopTime.setVisibility(View.GONE);
            view.advanceTimeButton.setVisibility(View.VISIBLE);
        });
        view.advanceTimeButton.setOnClickListener(v -> model.advanceTime());
         */
    }
    private void end() {
        started = false;
        //view.advanceTimeButton.setVisibility(View.INVISIBLE);
        //view.stopTime.setVisibility(View.INVISIBLE);
        view.startButton.setVisibility(View.INVISIBLE);
        view.addTaskButton.setVisibility(View.VISIBLE);
        view.endButton.setText("Routine Ended");
        view.endButton.setEnabled(false);
        view.endButton.requestLayout();
        view.resetButton.setVisibility(View.VISIBLE);
        view.homeButton.setVisibility(View.VISIBLE);
        view.goalTime.setOnClickListener(v -> {
            var dialogFragment = EditGoalTimeDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "EditGoalTimeDialogFragment");
        });
        model.endRoutine();
    }

    private void start() {
        started = true;
        view.startButton.setVisibility(View.INVISIBLE);
        view.endButton.setText("End");
        view.endButton.setVisibility(View.VISIBLE);
        view.endButton.setEnabled(true);
        view.addTaskButton.setVisibility(View.INVISIBLE);
        //view.stopTime.setVisibility(View.VISIBLE);
        //view.advanceTimeButton.setVisibility(View.VISIBLE);
        view.homeButton.setVisibility(View.INVISIBLE);
        view.goalTime.setOnClickListener(null);
        model.startRoutine();
    }
    private void reset() {
        started = null;
        view.startButton.setVisibility(View.VISIBLE);
        view.startButton.setEnabled(true);
        view.endButton.setVisibility(View.INVISIBLE);
        view.addTaskButton.setVisibility(View.VISIBLE);
        view.addTaskButton.setEnabled(true);
        //view.stopTime.setVisibility(View.INVISIBLE);
        //view.advanceTimeButton.setVisibility(View.INVISIBLE);
        view.homeButton.setVisibility(View.VISIBLE);
        view.resetButton.setVisibility(View.INVISIBLE);
        view.goalTime.setOnClickListener(v -> {
            var dialogFragment = EditGoalTimeDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "EditGoalTimeDialogFragment");
        });
        model.reset();
    }
}