package edu.ucsd.cse110.habitizer.lib.domain;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a person with a forename, surname, and age.
 * <p>
 * This is intended as an EXAMPLE of how you can use Java records.
 * <p>
 * Also note how this documentation is rendered when you hover over "Person".
 *
 * @param name       The name of the person. Must not be blank.
 * @param age        The age of the person. Must be non-negative.
 * @param bestFriend The best friend of the person, may be null.
 */
public record Person(
        @NotNull String name,
        int age,
        @Nullable Person bestFriend
) {
    /**
     * Canonical constructor for {@link Person}, can be used to enforce validations.
     */
    public Person {
        if (name.isBlank()) throw new IllegalArgumentException("Name must not be blank");
        if (age < 0) throw new IllegalArgumentException("Age must be non-negative");
    }

    /**
     * Additional constructor for {@link Person} that defaults the best friend to null.
     */
    public Person(String name, int age) {
        this(name, age, null);
    }

    public Person withName(String name) {
        return new Person(name, this.age, this.bestFriend);
    }

    public Person withAge(int age) {
        return new Person(this.name, age, this.bestFriend);
    }

    public Person withBestFriend(Person bestFriend) {
        return new Person(this.name, this.age, bestFriend);
    }
}