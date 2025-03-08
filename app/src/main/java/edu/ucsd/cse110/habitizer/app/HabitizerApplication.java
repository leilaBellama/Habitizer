package edu.ucsd.cse110.habitizer.app;

import android.app.Application;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.Repository;
public class HabitizerApplication extends Application {
    private InMemoryDataSource dataSource;
    private Repository repository;

    @Override
    public void onCreate() {
        super.onCreate();

        this.dataSource = InMemoryDataSource.DEFAULT();
        this.repository = new Repository(dataSource);
    }

    public Repository getTaskRepository() {
        return repository;
    }
}
