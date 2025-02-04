package edu.ucsd.cse110.observables;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * These tests aren't written in BDD style like the others.
 * They don't fit it as well as they're not testing objects but
 * functions that exist outside any particular objects.
 */

@DisplayName("Transformations")
public class TransformationsTests {
    @AfterAll
    static void cleanup() {
        // For finalizer coverage. Don't worry about this.
        System.gc();
    }

    @Test
    @DisplayName("mapping (map)")
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
    @DisplayName("switching (switchMap)")
    void testSwitchMap() {
        var left = new PlainMutableSubject<String>();
        var right = new PlainMutableSubject<String>();
        var trigger = new PlainMutableSubject<Boolean>(); // False is Left, Right is True

        var output = Transformations.switchMap(trigger, b -> b ? right : left);

        var leftLatch = new CountDownLatch(1);
        var rightLatch = new CountDownLatch(1);
        var outputLatch = new CountDownLatch(2);

        // Expects just "SENT TO LEFT"
        left.observe(value -> {
            assertThat(value, is("SENT TO LEFT"));
            leftLatch.countDown();
        });

        // Expects just "SENT TO RIGHT"
        right.observe(value -> {
            assertThat(value, is("SENT TO RIGHT"));
            rightLatch.countDown();
        });

        // Expects to get forwarded one and then the other
        output.observe(value -> {
            switch ((int) outputLatch.getCount()) {
                case 2: // first
                    assertThat(value, is("SENT TO LEFT"));
                    break;
                case 1: // second
                    assertThat(value, is("SENT TO RIGHT"));
                    break;
                default: // never!
                    fail();
            }
            outputLatch.countDown(); // 2 -> 1 -> 0
        });

        assertDoesNotThrow(() -> {
            trigger.setValue(false);
            left.setValue("SENT TO LEFT");
            leftLatch.await(1, TimeUnit.SECONDS);
            assertThat(output.getValue(), is("SENT TO LEFT"));

            trigger.setValue(true);
            right.setValue("SENT TO RIGHT");
            rightLatch.await(1, TimeUnit.SECONDS);
            assertThat(output.getValue(), is("SENT TO RIGHT"));

            // Wait for everything to settle.
            outputLatch.await(1, TimeUnit.SECONDS);

            // All have counted down fully.
            assertThat(leftLatch.getCount(), is(0L));
            assertThat(rightLatch.getCount(), is(0L));
            assertThat(outputLatch.getCount(), is(0L));
        });
    }
}
