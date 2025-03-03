package edu.ucsd.cse110.habitizer.app;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import edu.ucsd.cse110.habitizer.app.databinding.ActivityMainBinding;
import edu.ucsd.cse110.habitizer.app.ui.HomePageFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding view;
    private boolean onHomePage = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);

        this.view = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(view.getRoot());
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        var item = menu.findItem(R.id.action_bar_menu_swap_views);
        Log.d("MA","menu " + onHomePage);
        item.setVisible(!onHomePage);
        item.setEnabled(!onHomePage);
        onHomePage = !onHomePage;
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        var itemId = item.getItemId();
        if (itemId == R.id.action_bar_menu_swap_views) {

            swapFragments();
        }
        return super.onOptionsItemSelected(item);
    }


    public void swapFragments() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.Home_page_fragment_container, HomePageFragment.newInstance())
                .commit();

        invalidateOptionsMenu();

    }

     */

}