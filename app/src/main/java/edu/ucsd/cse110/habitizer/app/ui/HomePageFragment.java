package edu.ucsd.cse110.habitizer.app.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentHomePageBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.util.Observer;


public class HomePageFragment extends Fragment {
    private FragmentHomePageBinding view;
    private MainViewModel model;
    private final Observer<List<Routine>> routineObserver = routines -> {
        if (routines != null) {
            updateRoutineButtons(routines);
        }
    };

    public HomePageFragment() {
    }

    public static HomePageFragment newInstance() {
        HomePageFragment fragment = new HomePageFragment();
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
        view = FragmentHomePageBinding.inflate(inflater,container,false);

        setupMVP();
        return view.getRoot();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        model.getRoutines().removeObserver(routineObserver);
        Log.d("home frag", "destroyed");
    }

    private void setupMVP(){
        model.getRoutines().observe(routineObserver);
        view.newRoutineButton.setOnClickListener(v -> model.newRoutine());
    }
    private void updateRoutineButtons(List<Routine> routines) {
        view.HomePageButtonsLayout.removeAllViews();
        for (Routine routine : routines) {
            addRoutineButton(routine);
        }
    }
    private void addRoutineButton(Routine routine) {
        Button button = new Button(requireActivity());
        button.setText(routine.getName());
        button.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        button.setOnClickListener(view -> {
            model.setRoutineId(routine.getId());
            switchFragment();
        });
        view.HomePageButtonsLayout.addView(button);
    }
    private void switchFragment() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.Home_page_fragment_container, RoutinesPageFragment.newInstance())
                .commit();
    }
}