package edu.ucsd.cse110.observables.android;

import edu.ucsd.cse110.observables.Observer;

/**
 * Wrapper class for an {@link androidx.lifecycle.Observer} that adapts it
 * to an {@link Observer}. Used to preserve the ability to unregister observers.
 *
 * By value semantics, two LiveDataObserverAdapters are equal if they wrap the same
 * {@link androidx.lifecycle.Observer}. This is important for unregistering observers.
 *
 * @param <T>
 */
record LiveDataObserverAdapter<T>(
        androidx.lifecycle.Observer<? super T> wrappedAndroidObserver
) implements Observer<T> {

    @Override
    public void onChanged(T value) {
        wrappedAndroidObserver.onChanged(value);
    }
}
