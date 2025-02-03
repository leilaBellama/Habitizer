package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Test;

/**
 * Example test demonstrating use of Hamcrest matchers (which are also
 * used for espresso assertions in instrumented Android UI tests).
 */
public class PersonTest {
    private final Person mudasir = new Person("Mudasir", 22);
    private final Person asif = new Person("Asif", 21, mudasir);
    private final Person salman = new Person("Salman", 20);

    @Test
    public void testFriendshipEnded() {
        var mudasir = new Person("Mudasir", 22);
        var asif = new Person("Asif", 21);
        var salman = new Person("Salman", 20);

        // GIVEN Asif's best friend is Mudasir.
        asif = asif.withBestFriend(mudasir);
        mudasir = mudasir.withBestFriend(asif);

        // WHEN Friendship ended with Mudasir, now Salman is Asif's best friend.
        asif = asif.withBestFriend(salman);
        mudasir = mudasir.withBestFriend(null);

        // THEN Salman is Asif's best friend.
        // AND Mudasir has no friend.
        assertThat(asif.bestFriend(), is(salman));
        assertThat(mudasir.bestFriend(), is(nullValue()));
    }

    @Test
    public void testValidationErrorEmptyName() {
        // GIVEN a person with a blank name.
        // THEN an IllegalArgumentException is thrown.
        assertThrows(IllegalArgumentException.class, () -> new Person("", 22));
    }

    @Test
    public void testValidationErrorNegativeAge() {
        // GIVEN a person with a negative age.
        // THEN an IllegalArgumentException is thrown.
        assertThrows(IllegalArgumentException.class, () -> new Person("Mudasir", -22));
    }
}