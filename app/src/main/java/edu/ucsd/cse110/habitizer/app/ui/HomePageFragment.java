package edu.ucsd.cse110.habitizer.app.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Collections;
import java.util.List;

import edu.ucsd.cse110.habitizer.app.MainActivity;
import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentHomePageBinding;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentRoutinesPageBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;


public class HomePageFragment extends Fragment {
    private FragmentHomePageBinding view;
    private MainViewModel model;

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
    /*
    public void onCreateMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_bar, menu);
        //super.onCreateOptionsMenu(menu, inflater);
    }

    public void onPrepareMenu(Menu menu) {
        //super.onPrepareMenu(menu);
        var item = menu.findItem(R.id.action_bar_menu_swap_views);
        item.setVisible(!onHomePage);
        item.setEnabled(!onHomePage);
        //return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        var itemId = item.getItemId();
        if (itemId == R.id.action_bar_menu_swap_views) {
            onHomePage = false;
            invalidateOptionsMenu();
            swapFragments();
        }
        return super.onOptionsItemSelected(item);
    }

     */
    @Override
    public void onDestroyView(){
        super.onDestroyView();

        //requireActivity().invalidateOptionsMenu();
        model.getRoutines().removeObservers();
    }

    private void setupMVP(){
        model.getRoutines().observe(routines -> {
            if (routines == null) return;
            updateRoutineButtons(routines);
        });
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
            //model.swapCurrentRoutine(routine.getId());
            model.setRoutineId(routine.getId());
            switchFragment();
            //((MainActivity) requireActivity()).swapFragments();
            //Collections.swap(model.getRoutines().getValue(),0,routine.getId());
        });
        view.HomePageButtonsLayout.addView(button);
    }
    private void switchFragment() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.Home_page_fragment_container, new RoutinesPageFragment())
                .commit();
    }
}