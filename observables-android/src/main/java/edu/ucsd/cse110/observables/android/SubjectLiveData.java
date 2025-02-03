package edu.ucsd.cse110.observables.android;

import androidx.lifecycle.LiveData;

import edu.ucsd.cse110.observables.Subject;

/**
 * This class wraps a {@link Subject}, for use where an
 * Android {@link androidx.lifecycle.LiveData} is expected.
 *
 * @param <T> The type of the value that the subject holds.
 */
public class SubjectLiveData<T> extends LiveData<T> {
    private final Subject<T> subject;

    public SubjectLiveData(Subject<T> subject) {
        this.subject = subject;
        this.subject.observe(this::setValue);
    }

    protected void finalize() {
        this.subject.removeObserver(this::setValue);
    }
}
