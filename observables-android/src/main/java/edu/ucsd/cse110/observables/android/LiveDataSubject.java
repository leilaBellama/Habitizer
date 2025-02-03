package edu.ucsd.cse110.observables.android;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.TestOnly;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import edu.ucsd.cse110.observables.Observer;
import edu.ucsd.cse110.observables.Subject;

/**
 * This class wraps a {@link androidx.lifecycle.LiveData} object, for use where an
 * {@link edu.ucsd.cse110.observables.Subject} is expected. It is a bridge between the
 * Android LiveData and our own Subject interfaces.
 *
 * @param <T> The type of the value that the subject holds.
 */
public class LiveDataSubject<T> implements Subject<T> {
    // Wrapped LiveData object.
    protected final LiveData<T> wrapped;

    // Our own tally of observers separate from LiveData's. CLQ for thread safety.
    private final Queue<LiveDataObserverAdapter<? super T>> observers = new ConcurrentLinkedQueue<>();

    public LiveDataSubject(LiveData<T> wrapped) {
        if (wrapped.hasObservers()) {
            // If the LiveData object already has observers, it is unsafe to wrap it,
            // because we cannot guarantee that the observers will be removed from the
            // LiveData object when they are removed from this Subject.

            var msg = "LiveData object already has observers. It is unsafe to wrap it.";
            throw new IllegalArgumentException(msg);
        }

        this.wrapped = wrapped;
    }

    @Override
    public @Nullable T getValue() {
        return wrapped.getValue();
    }

    @Override
    public boolean hasObservers() {
        return wrapped.hasObservers();
    }

    @Override
    public boolean isInitialized() {
        return wrapped.isInitialized();
    }

    @Override
    @MainThread
    public Observer<? super T> observe(@NonNull Observer<? super T> observer) {

        androidx.lifecycle.Observer<? super T> androidObserver;
        LiveDataObserverAdapter<? super T> adapter;

        if (observer instanceof LiveDataObserverAdapter<? super T>) {
            // This is already a LiveDataObserverAdapter, there is need to re-wrap it.
            adapter = (LiveDataObserverAdapter<? super T>) observer;
            androidObserver = adapter.wrappedAndroidObserver();
        } else {
            // This is our own Observer, we need to wrap it.
            androidObserver = (androidx.lifecycle.Observer<T>) observer::onChanged;
            adapter = new LiveDataObserverAdapter<>(androidObserver);
        }

        wrapped.observeForever(androidObserver);
        observers.add(adapter);
        return adapter;
    }

    @Override
    @MainThread
    public void removeObserver(@NonNull Observer<? super T> observer) {
        if (observer instanceof LiveDataObserverAdapter<? super T> adapter) {
            wrapped.removeObserver(adapter.wrappedAndroidObserver());
            observers.removeIf(o -> o == adapter);
            return;
        }

        // This observer was cannot have been registered to this subject,
        // because it wasn't wrapped by us in the first place.
        var msg = "Cannot remove observer that was not registered to this subject.";
        throw new IllegalArgumentException(msg);
    }

    @Override
    @MainThread
    public void removeObservers() {
        while (!observers.isEmpty()) {
            removeObserver(observers.remove()); // NB: remove() is pop()
        }
    }

    @Override
    @TestOnly
    public List<Observer<? super T>> getObservers() {
        return List.copyOf(observers);
    }
}
