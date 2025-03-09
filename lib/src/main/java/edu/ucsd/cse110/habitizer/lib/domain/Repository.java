package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.util.Subject;
public class Repository {
    private final InMemoryDataSource dataSource;

    public Repository(InMemoryDataSource dataSource){
        this.dataSource = dataSource;
    }

    public Integer count() {return dataSource.getRoutines().size();}

    //public Subject<Integer> count() {return dataSource.getCountSubject();}

    public Subject<Routine> find(int id) {return dataSource.getRoutineSubject(id);}
    public Subject<List<Routine>> findAll() {return dataSource.getAllRoutinesSubject();}

    public void save(Routine routine) {dataSource.putRoutine(routine);}
    public void save(List<Routine> routines) {
        for(var routine : routines){
            dataSource.putRoutine(routine);
        }
    }

    public void remove(int id) {dataSource.removeRoutine(id);}

}
