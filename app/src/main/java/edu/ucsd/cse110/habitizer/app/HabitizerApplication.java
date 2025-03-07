package edu.ucsd.cse110.habitizer.app;

import android.app.Application;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.Repository;
import edu.ucsd.cse110.habitizer.lib.domain.SimpleRepository;

public class HabitizerApplication extends Application {
    private InMemoryDataSource dataSource;
    private Repository repository;

    @Override
    public void onCreate() {
        super.onCreate();

        this.dataSource = InMemoryDataSource.DEFAULT();
        this.repository = new SimpleRepository(dataSource);
    }

    public Repository getTaskRepository() {
        return repository;
    }
}
