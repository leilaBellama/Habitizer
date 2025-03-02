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

import java.util.Collections;
import java.util.List;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentHomePageBinding;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentRoutinesPageBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePageFragment extends Fragment {
    private FragmentHomePageBinding view;
    private MainViewModel model;

    private List<Routine> routineList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomePageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomePageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomePageFragment newInstance(String param1, String param2) {
        HomePageFragment fragment = new HomePageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        model.getRoutines().removeObservers();
        Log.d("HPF", "destroyed");
    }

    private void setupMVP(){
        model.getRoutines().observe(routines -> {
            if (routines == null) return;
            updateRoutineButtons(routines);
        });
        view.newRoutineButton.setOnClickListener(v -> model.newRoutine());
    }
    private void updateRoutineButtons(List<Routine> routines) {
        // Clear existing buttons to avoid duplication
        view.HomePageButtonsLayout.removeAllViews();

        // Add a button for each routine
        for (Routine routine : routines) {
            addRoutineButton(routine);
        }
    }
    private void addRoutineButton(Routine routine) {
        // Create a new button
        Button button = new Button(requireActivity());
        button.setText(routine.getName());
        button.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Set an OnClickListener for each button
        button.setOnClickListener(view -> {
            // Perform action when button is clicked
            switchFragment();
            Collections.swap(model.getRoutines().getValue(),0,routine.getId());
            //System.out.println(routine.getId() + "Clicked on: " + routine.getName() );
        });

        // Add button to the layout
        view.HomePageButtonsLayout.addView(button);
    }
    private void switchFragment() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.Home_page_fragment_container, new RoutinesPageFragment()) // Swap to the new Fragment
                //.addToBackStack(null) // Enables back navigation
                .commit();
    }
}