package com.walkmates.lab1;

import com.walkmates.model.Seeker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

/**
 * Lab 1, Part B — specification-based tests for {@link Seeker}.
 *
 * <p>Design your tests on paper first (equivalence partitions, boundary values, decision table)
 * from {@code docs/REQUIREMENTS.md} FR-1.1 / FR-1.3 / FR-1.2, then implement them here. One
 * worked example is provided; the {@code TODO}s are yours.</p>
 */
class SeekerSpecBasedTest {

    // ---- Worked example: boundary value at the maximum single top-up (FR-1.3) ----
    @Test
    @DisplayName("Top-up exactly at the 5000 SEK single-transaction maximum is accepted")
    void topUpAtSingleMaximumIsAccepted() {
        Seeker seeker = new Seeker("sam@example.com", "Sam", "0707654321");

        seeker.addFunds(Seeker.MAX_SINGLE_TOP_UP); // 5000.00, the boundary value

        assertThat(seeker.getBalance()).isEqualTo(Seeker.MAX_SINGLE_TOP_UP);
    }

    // TODO (EP): one valid + one invalid equivalence class for email, name, and phone (FR-1.1).
    // TODO (BVA): just-below / at / just-above the 10.00 minimum top-up (FR-1.3).
    // TODO (BVA): a top-up that would push the balance above 20000.00 is rejected (FR-1.3).
    // TODO (Decision table): expected fee + max-bookings for each trust tier (FR-1.2).

    @Test
    @DisplayName("TODO: replace me — invalid email is rejected at registration")
    void invalidEmailIsRejected() {
        // Example of the shape; expand into your full EP set.
        assertThrows(IllegalArgumentException.class,
                () -> new Seeker("not-an-email", "Sam", "0707654321"));
    }

    @Test
    @DisplayName("Adding 250 SEK to a new seeker gives a 250.00 balance")
    void addingFundsWorks() {
        Seeker seeker = new Seeker("you@example.com", "You", "0701234567");  // Arrange
        seeker.addFunds(250.00);                                              // Act
        assertThat(seeker.getBalance()).isEqualTo(250.00);                   // Assert
    }


    private static final String VALID_EMAIL = "user@example.com";
    private static final String VALID_NAME = "Anna-Marie O'Neil";
    private static final String VALID_PHONE = "0701234567";

    // ---- Activity 2.1: Equivalence partitioning (FR-1.1) ----

    @Test
    @DisplayName("A valid email format and length is accepted")
    void validEmailIsAccepted() {
        Seeker seeker = new Seeker(VALID_EMAIL, VALID_NAME, VALID_PHONE);

        assertThat(seeker.getEmail()).isEqualTo(VALID_EMAIL);
    }

    @ParameterizedTest(name = "Invalid email: {0}")
    @MethodSource("invalidEmails")
    @DisplayName("Invalid email classes are rejected")
    void invalidEmailIsRejected(String email) {
        assertThrows(IllegalArgumentException.class,
                () -> new Seeker(email, VALID_NAME, VALID_PHONE));
    }

    private static Stream<String> invalidEmails() {
        return Stream.of(
                "user.example.com",                    // missing @
                "user@examplecom",                     // domain has no dot
                "@example.com",                        // empty local part
                "a".repeat(24) + "@example.com"       // 255 characters; maximum is 254
        );
    }

    @ParameterizedTest(name = "Valid display name: {0}")
    @ValueSource(strings = {"Anna", "Anna-Marie O'Neil"})
    @DisplayName("Valid display-name classes are accepted")
    void validDisplayNameIsAccepted(String displayName) {
        Seeker seeker = new Seeker(VALID_EMAIL, displayName, VALID_PHONE);

        assertThat(seeker.getDisplayName()).isEqualTo(displayName);
    }

    @ParameterizedTest(name = "Invalid display name: {0}")
    @MethodSource("invalidDisplayNames")
    @DisplayName("Invalid display-name classes are rejected")
    void invalidDisplayNameIsRejected(String displayName) {
        assertThrows(IllegalArgumentException.class,
                () -> new Seeker(VALID_EMAIL, displayName, VALID_PHONE));
    }

    private static Stream<String> invalidDisplayNames() {
        return Stream.of(
                "A",                  // shorter than 2 characters
                "A".repeat(41),       // longer than 40 characters
                "Anna123#@"           // characters outside the valid class
        );
    }

    @ParameterizedTest(name = "Valid phone number: {0}")
    @ValueSource(strings = {"0701234567", "+46701234567"})
    @DisplayName("Valid Swedish and international phone formats are accepted")
    void validPhoneNumberIsAccepted(String phoneNumber) {
        Seeker seeker = new Seeker(VALID_EMAIL, VALID_NAME, phoneNumber);

        assertThat(seeker.getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @ParameterizedTest(name = "Invalid phone number: {0}")
    @ValueSource(strings = {"0501234567", "+1231234567", "070123456", "07012345678",
            "+4670123456", "+467012345678"})
    @DisplayName("Invalid phone-format and length classes are rejected")
    void invalidPhoneNumberIsRejected(String phoneNumber) {
        assertThrows(IllegalArgumentException.class,
                () -> new Seeker(VALID_EMAIL, VALID_NAME, phoneNumber));
    }
}
