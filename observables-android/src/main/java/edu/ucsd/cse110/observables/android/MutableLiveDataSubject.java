package edu.ucsd.cse110.observables.android;

import androidx.lifecycle.MutableLiveData;

import edu.ucsd.cse110.observables.MutableSubject;

public class MutableLiveDataSubject<T> extends LiveDataSubject<T> implements MutableSubject<T> {
    protected MutableLiveData<T> wrapped;

    public MutableLiveDataSubject() {
        super(new MutableLiveData<>());
        this.wrapped = (MutableLiveData<T>) super.wrapped;
    }

    public MutableLiveDataSubject(T initialValue) {
        super(new MutableLiveData<>(initialValue));
        this.wrapped = (MutableLiveData<T>) super.wrapped;
    }

    public MutableLiveDataSubject(MutableLiveData<T> mutableLiveData) {
        super(mutableLiveData);
        this.wrapped = (MutableLiveData<T>) super.wrapped;
    }

    @Override
    public void setValue(T newValue) {
        wrapped.setValue(newValue);
    }
}
