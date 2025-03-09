package edu.ucsd.cse110.habitizer.lib.util;

import androidx.annotation.Nullable;
//import androidx.lifecycle.LifecycleOwner;

public interface Subject<T> {
    @Nullable
    T getValue();

    void observe(Observer<T> observer);

    void removeObserver(Observer<T> observer);

    //void removeAllObservers(LifecycleOwner owner);

}
