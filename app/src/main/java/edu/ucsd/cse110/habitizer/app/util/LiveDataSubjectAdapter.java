package edu.ucsd.cse110.habitizer.app.util;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.util.Observer;
import edu.ucsd.cse110.habitizer.lib.util.Subject;

public class LiveDataSubjectAdapter<T> implements Subject<T> {
    private final LiveData<T> adaptee;
    private final List<Observer<T>> observers = new java.util.ArrayList<>();

    public LiveDataSubjectAdapter(LiveData<T> adaptee){
        this.adaptee = adaptee;
    }

    @Nullable
    @Override
    public T getValue(){
        return adaptee.getValue();
    }

    @Override
    public void observe(Observer<T> observer){
        observers.add(observer);
        adaptee.observeForever(observer::onChange);
    }

    @Override
    public void removeObserver(Observer<T> observer){
        observers.remove(observer);
        adaptee.observeForever(observer::onChange);
    }

    @Override
    public Boolean hasObservers(){
        return adaptee.hasObservers();
    }

}
