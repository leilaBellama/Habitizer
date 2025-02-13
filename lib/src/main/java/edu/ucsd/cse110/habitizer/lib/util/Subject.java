package edu.ucsd.cse110.habitizer.lib.util;

import androidx.annotation.Nullable;

import java.util.List;
public class Subject<T> {
    private @Nullable T value = null;
    private final List<Observer<T>> observers = new java.util.ArrayList<>();

    @Nullable
    public T getValue(){
        return value;
    }

    private void notifyObservers(){
        for(var observer: observers){
            observer.onChange(value);
        }
    }

    public void setValue(T value){
        this.value = value;
        notifyObservers();
    }

    public void observe(Observer<T> observer){
        observers.add(observer);
        observer.onChange(value);
    }

    public void removeObserver(Observer<T> observer){
        observers.remove(observer);
    }
    public void removeObservers(){
        for (var observer : observers){
            observers.remove(observer);
        }
    }


}
