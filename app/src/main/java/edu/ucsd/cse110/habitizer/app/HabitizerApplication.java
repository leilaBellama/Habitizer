package edu.ucsd.cse110.habitizer.app;

import android.app.Application;

import androidx.room.Room;

import edu.ucsd.cse110.habitizer.app.data.db.HabitizerDatabase;
import edu.ucsd.cse110.habitizer.app.data.db.RoomRoutineRepository;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.Repository;
import edu.ucsd.cse110.habitizer.lib.domain.SimpleRepository;

public class HabitizerApplication extends Application {
    private InMemoryDataSource dataSource;
    private Repository repository;

    @Override
    public void onCreate() {
        super.onCreate();

        //this.dataSource = InMemoryDataSource.DEFAULT();
        //this.repository = new SimpleRepository(dataSource);

        var database = Room.databaseBuilder(
                getApplicationContext(),
                HabitizerDatabase.class,
                "habitizer-database"
        )
                .allowMainThreadQueries()
                .build();

        //this.dataSource = InMemoryDataSource.DEFAULT_ROUTINES();
        this.repository = new RoomRoutineRepository(database.routineDao());

        var sharedPreferences = getSharedPreferences("habitizer", MODE_PRIVATE);
        var isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);

        if(isFirstRun && database.routineDao().countRoutines() == 0){
            repository.saveRoutines(InMemoryDataSource.DEFAULT_ROUTINES);
            repository.saveTasks(InMemoryDataSource.DEFAULT_TASKS);
            sharedPreferences.edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }

    }

    public Repository getTaskRepository() {
        return repository;
    }
}
