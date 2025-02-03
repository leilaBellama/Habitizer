package edu.ucsd.cse110.observables;

import java.util.function.Function;

/**
 * Utility methods for working with subjects.
 */
public final class Subjects {
    /**
     * Applies the given function on the main thread to each value emitted by
     * the source Subject and returns a Subject that emits resulting values.
     * <p>
     * This allows for "chaining" of Subjects, where the output of one Subject
     * is used as the input to the new returned Subject.
     *
     * @param subject The subject to map.
     * @param transformation The function to map the subject with.
     * @return A new subject which has
     * @param <T> The type of the values of source subject.
     * @param <R> The type of the values of the result subject.
     */
    public static <T, R> Subject<R> map(Subject<T> subject, Function<T, R> transformation) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Advanced fancy transformation. Returns a new {@link Subject} that "switches"
     * to different {@link Subject}s depending on a trigger {@link Subject}.
     * <p>
     * This allows for dynamic switching of the source Subject based on the values
     * emitted by the trigger Subject.
     * <p>
     * If you are into functional programming, this is vaguely similar to a flatMap.
     *
     * @param trigger The subject that triggers the switch.
     * @param transformation A function to apply to each value set on source to create a new delegate LiveData for the returned one.
     * @return A new subject that emits values from a source subject based on the trigger subject.
     * @param <T> The type of the trigger subject.
     * @param <R> The type of the resulting subject.
     */
    public static <T, R> Subject<R> switchMap(Subject<T> trigger, Function<T, Subject<R>> transformation) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
