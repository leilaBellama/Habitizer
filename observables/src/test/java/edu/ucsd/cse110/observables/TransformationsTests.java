package edu.ucsd.cse110.observables;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * These tests aren't written in BDD style like the others.
 * They don't fit it as well as they're not testing objects but
 * functions that exist outside any particular objects.
 */
public class TransformationsTests {
    @Test
    void testMap() {
        var input = new PlainMutableSubject<String>();

        var output = Transformations.map(input, String::toUpperCase);
        if (output instanceof PlainMediatorSubject<String> mediator) {
            assertThat(mediator.getSourceSubjects(), containsInAnyOrder(input));
        }

        var latch = new CountDownLatch(1);
        output.observe(value -> {
            assertThat(value, is("HELLO WORLD!"));
        });
        assertDoesNotThrow(() -> {
            input.setValue("hello world!");
            latch.await(1, TimeUnit.SECONDS);
        }, "Timed out awaiting observer notification");
    }

    @Test
    void testSwitchMap() {
        var left = new PlainMutableSubject<String>();
        var right = new PlainMutableSubject<String>();
        var trigger = new PlainMutableSubject<Boolean>(); // False is Left, Right is True

        var output = Transformations.switchMap(trigger, b -> b ? right : left);

        var latch = new CountDownLatch(2);

        left.observe(value -> {
            assertThat(value, is("SENT TO LEFT"));
            latch.countDown();
        });

        right.observe(value -> {
            assertThat(value, is("SENT TO RIGHT"));
            latch.countDown();
        });

        assertDoesNotThrow(() -> {
            trigger.setValue(false);
            left.setValue("SENT TO LEFT");

            trigger.setValue(true);
            right.setValue("SENT TO RIGHT");

            latch.await(1, TimeUnit.SECONDS);
        });
    }

    @AfterAll
    static void cleanup() {
        // For finalizer coverage. Don't worry about this.
        System.gc();
    }
}
