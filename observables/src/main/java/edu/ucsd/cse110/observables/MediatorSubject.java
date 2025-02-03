package edu.ucsd.cse110.observables;

import androidx.annotation.MainThread;
import androidx.annotation.VisibleForTesting;

import java.util.List;

/**
 * Interface for mediator subjects. This is an advanced subject that observes other subjects,
 * which you will likely not ever need to use directly. It's used to implement some utility
 * methods.
 *
 * @param <T> The type of the value that the subject holds.
 */
public interface MediatorSubject<T> extends MutableSubject<T> {
    /**
     * Register a subject subject to be observed.
     * <p>
     * @param subject  The source subject to observe.
     * @param observer The observer to notify when the source subject changes.
     * @param <S>      The type of the subject subject.
     * @return The observer that was registered, so that it can be unregistered later.
     */
    @MainThread
    <S> Observer<? super S> addSource(Subject<S> subject, Observer<? super S> observer);


    /**
     * Unregister a source subject that was previously registered.
     *
     * @param source The source subject to stop observing.
     * @param <S>    The type of the source subject.
     */
    @MainThread
    <S> void removeSource(Subject<S> source);

    /**
     * Get the list of sources. This method is for testing purposes only.
     *
     * @return The list of sources.
     */
    @VisibleForTesting
    List<? extends Observer<?>> getSources();
}