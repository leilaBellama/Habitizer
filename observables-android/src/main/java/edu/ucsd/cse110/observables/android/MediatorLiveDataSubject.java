package edu.ucsd.cse110.observables.android;

import androidx.annotation.Nullable;
import androidx.lifecycle.MediatorLiveData;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import edu.ucsd.cse110.observables.MediatorSubject;
import edu.ucsd.cse110.observables.Observer;
import edu.ucsd.cse110.observables.Subject;

public class MediatorLiveDataSubject<T> extends MutableLiveDataSubject<T> implements MediatorSubject<T> {

    // A private record to store the source and observer of a LiveData.
    private record Source<S>(
            Subject<S> source,
            Observer<? super S> observer
    ) implements Observer<S> {
        @Override
        public void onChanged(@Nullable S value) {
            observer.onChanged(value);
        }
    }

    protected final MediatorLiveData<T> wrapped;
    private final Queue<Source<?>> sources = new ConcurrentLinkedQueue<>();

    public MediatorLiveDataSubject() {
        super(new MediatorLiveData<>());
        this.wrapped = (MediatorLiveData<T>) super.wrapped;
    }

    @Override
    public <S> Observer<? super S> addSource(Subject<S> subject, Observer<? super S> observer) {
        var source = new Source<>(subject, observer);

        androidx.lifecycle.LiveData<S> androidSubject;
        if (subject instanceof LiveDataSubject<S> adapterSubject) {
            // We have a LiveDataSubject, so we can just use its wrapped LiveData.
            androidSubject = adapterSubject.wrapped;
        } else {
            // We just have a plain Subject, so we need to adapt it.
            androidSubject = new SubjectLiveData<>(subject);
        }


        wrapped.addSource(androidSubject, observer::onChanged);
        sources.add(source);

        return source;
    }

    @Override
    public <S> void removeSource(Subject<S> source) {
        sources.removeIf(s -> s.source == source);
    }

    @Override
    public List<? extends Observer<?>> getSources() {
        return List.copyOf(sources);
    }
}
