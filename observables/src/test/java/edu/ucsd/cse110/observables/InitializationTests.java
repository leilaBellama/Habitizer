package edu.ucsd.cse110.observables;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@DisplayName("Subject Initialization")
class InitializationTests {
    @Nested
    @DisplayName("Given a new empty subject")
    class GivenNewEmptySubject {
        Subject<String> subject = new PlainMutableSubject<>();

        @Test
        @DisplayName("Then it is not explicitly initialized")
        void ThenItIsInitialized() {
            assertThat(subject.isInitialized(), is(false));
        }

        @Test
        @DisplayName("Then it has null value")
        void ThenItHasNullValue() {
            assertThat(subject.getValue(), is(nullValue()));
        }
    }

    @Nested
    @DisplayName("Given a new subject with an initial value")
    class GivenNewSubjectWithInitialValue {
        Subject<String> subject = new PlainMutableSubject<>("Hello World!");

        @Test
        @DisplayName("Then it is explicitly initialized")
        void ThenItIsInitialized() {
            assertThat(subject.isInitialized(), is(true));
        }

        @Test
        @DisplayName("Then it has that value")
        void ThenItHasThatValue() {
            assertThat(subject.getValue(), is("Hello World!"));
        }


    }
}