package edu.ucsd.cse110.habitizer.app;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import edu.ucsd.cse110.habitizer.app.databinding.ActivityMainBinding;
import edu.ucsd.cse110.habitizer.app.ui.dialog.CreateTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.dialog.EditGoalTimeDialogFragment;
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

        model.getGoalTime().observe(goalTime -> {
            boolean isGoalTimeSet = goalTime != null && !goalTime.isEmpty();
            view.goalTime.setText(isGoalTimeSet ? goalTime + " min" : "Set");
            view.startButton.setEnabled(isGoalTimeSet && !model.getHasStarted().getValue());
        });

        //start button starts routine, removes switch routine and add option
        view.startButton.setOnClickListener(v -> {
            view.startButton.setEnabled(false);
            model.getElapsedTime().observe(time -> {
                if (time != null) {
                    view.time.setText(time + " min");
                }
            });
            model.startRoutine();
            started = true;
            invalidateOptionsMenu();
        });

        view.stopTime.setOnClickListener(v -> model.stopTimer());
        view.advanceTimeButton.setOnClickListener(v -> model.advanceTime());
        this.view.addTaskButton.setOnClickListener(v -> {
            if(started){
                view.addTaskButton.setEnabled(false);
            }else {
                var dialogFragment = CreateTaskDialogFragment.newInstance();
                dialogFragment.show(getSupportFragmentManager(), "CreateTaskDialogFragment");
            }
        });

        view.goalTime.setOnClickListener(v -> {
            var dialogFragment = EditGoalTimeDialogFragment.newInstance();
            dialogFragment.show(getSupportFragmentManager(), "EditGoalTimeDialogFragment");
        });


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