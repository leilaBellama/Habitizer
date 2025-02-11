package edu.ucsd.cse110.habitizer.app.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentMorningBinding;

public class MorningFragment extends Fragment {
    private FragmentMorningBinding view;
    private MainViewModel activityModel;


    public MorningFragment() {
        // Required empty public constructor
    }

    public static MorningFragment newInstance() {
        MorningFragment fragment = new MorningFragment();
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
        this.activityModel = modelProvider.get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = FragmentMorningBinding.inflate(inflater, container, false);
        setupMvp();
        return view.getRoot();
    }

    public void setupMvp() {
        //activityModel.getElapsedTime().observe(time -> view.timer.setText(time));

        view.startButton.setOnClickListener(v -> activityModel.startRoutine());
    }
}