package edu.ucsd.cse110.habitizer.app;

import android.os.Bundle;
import android.util.Log;
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
            if (hasStarted == null){
                reset();
                Log.d("MA start", " started is null");

            }
            else if (!hasStarted){
                endRoutine();
                Log.d("MA start", " started is false");

            }
            else {
                Log.d("MA start ", " started is true");
                startRoutine();
            }
        });
        model.getGoalTime().observe(goalTime -> {
            if(goalTime != null){
                view.goalTime.setText(goalTime + " min");
            }
        });
        model.getElapsedTime().observe(time -> {
            if (time != null) {
                runOnUiThread(() -> view.time.setText(time + " min"));
                Log.d("MA","obs time " + time);
            }
        });

        //start button starts routine, removes switch routine and add option
        view.startButton.setOnClickListener(v -> startRoutine());
        view.endButton.setOnClickListener(v -> endRoutine());
        view.stopTime.setOnClickListener(v -> model.stopTimer());
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

        /*
        var ld = new MutableLiveData<String>();
        ld.observe(this, (s) -> {
            System.out.println(s);
        });

         */

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
        view.stopTime.setVisibility(View.GONE);
        view.startButton.setVisibility(View.GONE);
        view.endButton.setText("Routine Ended");
        view.endButton.setVisibility(View.VISIBLE);
        view.endButton.setEnabled(false);
        view.endButton.requestLayout();
        model.endRoutine();
        started = false;
        invalidateOptionsMenu();
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
        started = true;
        invalidateOptionsMenu();
    }
    private void reset() {
        view.startButton.setVisibility(View.VISIBLE);
        view.startButton.setEnabled(true);
        view.endButton.setVisibility(View.GONE);
        view.addTaskButton.setVisibility(View.VISIBLE);
        view.addTaskButton.setEnabled(true);
        view.stopTime.setVisibility(View.GONE);
        view.advanceTimeButton.setVisibility(View.GONE);
        started = false;
        invalidateOptionsMenu();
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