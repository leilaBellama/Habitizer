package edu.ucsd.cse110.habitizer.app;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import edu.ucsd.cse110.habitizer.app.databinding.ActivityMainBinding;
import edu.ucsd.cse110.habitizer.app.ui.dialog.CreateTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.dialog.EditGoalTimeDialogFragment;

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
        model.getHasStarted().observe(hasStarted -> {
            if (hasStarted == null) return;
            if (!hasStarted) endRoutine();
        });
        model.getGoalTime().observe(goalTime -> {
            if(goalTime != null){
                view.goalTime.setText(goalTime + " min");
            }
        });

        view.startButton.setOnClickListener(v -> startRoutine());
        view.endButton.setOnClickListener(v -> endRoutine());
        view.stopTime.setOnClickListener(v -> {
            model.stopTimer();
            view.stopTime.setVisibility(View.GONE);
            view.advanceTimeButton.setVisibility(View.VISIBLE);
        });
        view.advanceTimeButton.setOnClickListener(v -> model.advanceTime());
        view.addTaskButton.setOnClickListener(v -> {
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

    private void endRoutine() {
        view.advanceTimeButton.setVisibility(View.GONE);
        view.endButton.setEnabled(false);
        view.endButton.setText("Routine Ended");
        view.endButton.requestLayout();
        model.endRoutine();
    }

    private void startRoutine() {
        view.startButton.setEnabled(false);
        view.startButton.setVisibility(View.INVISIBLE);
        view.endButton.setVisibility(View.VISIBLE);
        view.addTaskButton.setVisibility(View.GONE);
        view.stopTime.setVisibility(View.VISIBLE);
        model.getElapsedTime().observe(time -> {
            if (time != null) {
                runOnUiThread(() -> view.time.setText(time + " min"));
            }
        });
        model.startRoutine();
        started = true;
        invalidateOptionsMenu();
    }
}