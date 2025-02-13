package edu.ucsd.cse110.habitizer.app;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import edu.ucsd.cse110.habitizer.app.databinding.ActivityMainBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding view;
    private MainViewModel model;
    private boolean isShowingMorning = true;
    private boolean started = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);

        this.view = ActivityMainBinding.inflate(getLayoutInflater());

        var modelOwner = this;
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.model = modelProvider.get(MainViewModel.class);

        model.getRoutineTitle().observe(text -> view.routine.setText(text));
        model.getElapsedTime().observe(time -> {
            if (time != null) {
                view.timer.setText(time + " min");
            }
        });

        //start button starts routine and removes switch routine option
        view.startButton.setOnClickListener(v -> model.startRoutine());
        view.startButton.setOnClickListener(v -> {
            started = true;
            invalidateOptionsMenu();
        });
        view.addTaskButton.setOnClickListener(v -> model.addTask());
        view.stopTime.setOnClickListener(v -> model.stopTimer());
        view.advanceTimeButton.setOnClickListener(v -> model.advanceTime());


        setContentView(view.getRoot());

        var ld = new MutableLiveData<String>();
        ld.observe(this, (s) -> {
            System.out.println(s);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        var item = menu.findItem(R.id.action_bar_menu_swap_views);
        item.setVisible(!started);
        item.setEnabled(!started);
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        var itemId = item.getItemId();
        if (itemId == R.id.action_bar_menu_swap_views) {
            model.swapRoutine();
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    private void swapFragments() {
        if (isShowingMorning) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, EveningFragment.newInstance())
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, MorningFragment.newInstance())
                    .commit();
        }
        isShowingMorning = !isShowingMorning;
    }
     */
}