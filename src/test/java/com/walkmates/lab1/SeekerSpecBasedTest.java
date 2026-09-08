package com.walkmates.lab1;

import com.walkmates.model.Seeker;
import com.walkmates.model.TrustTier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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

    // ---- Seeker class constants ----
    /** A valid email used for creating a {@link Seeker}. */
    private static final String VALID_EMAIL = "user@example.com";
    /** A valid name used for creating a {@link Seeker}. */
    private static final String VALID_NAME = "Anna-Marie O'Neil";
    /** A valid Swedish-format phone number used for creating a {@link Seeker}. */
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
                "a".repeat(255) + "@example.com"       // 255 characters; maximum is 254
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
    // (BVA): a top-up that would push the balance above 20000.00 is rejected (FR-1.3).

    /**
     * Creates a valid {@link Seeker} and verifies that its constructor does not throw an exception.
     *
     * <p> The {@link Seeker} is constructed with the local valid test class constants
     * {@link #VALID_EMAIL}, {@link #VALID_NAME}, and {@link #VALID_PHONE}. </p>
     *
     * @return a valid new {@link Seeker} constructed with the valid test class constants.
     */
    private static Seeker newValidSeeker() {
        return assertDoesNotThrow(() -> new Seeker(VALID_EMAIL, VALID_NAME, VALID_PHONE));
    }

    /**
     * Verifies that top-up values at valid the maximum
     * and minimum boundaries are accepted and applied correctly.
     *
     * @param amount valid top-up value.
     */
    @ParameterizedTest(name = "Top-up {0} SEK is accepted")
    @CsvSource({"10.00", "10.01", "4999.99", "5000.00"})
    @DisplayName("Top-ups at valid minimum and maximum boundary values are accepted")
    void validTopUpBoundaryValuesAreAccepted(double amount) {
        Seeker seeker = newValidSeeker();

        seeker.addFunds(amount);

        assertThat(seeker.getBalance()).isEqualTo(amount);
    }

    /**
     * Verifies that top-up values outside the maximum
     * and minimum boundaries are rejected and do not mutate the Seeker's balance.
     *
     * @param amount invalid top-up value.
     */
    @ParameterizedTest(name = "Top-up {0} SEK is rejected")
    @CsvSource({"9.99", "5000.01"})
    @DisplayName("Top-ups just outside the transaction boundaries are rejected")
    void invalidTopUpBoundaryValuesAreRejected(double amount) {
        Seeker seeker = newValidSeeker();

        assertThrows(IllegalArgumentException.class, () -> seeker.addFunds(amount));
        assertThat(seeker.getBalance()).isZero();
    }

    /**
     * Verifies that a seeker's balance (19999.99) just below maximum capacity is accepted.
     */
    @Test
    @DisplayName("A resulting balance just below 20000 SEK is accepted")
    void balanceJustBelowMaximumIsAccepted() {
        Seeker seeker = newValidSeeker();
        seeker.addFunds(5000.00);
        seeker.addFunds(5000.00);
        seeker.addFunds(5000.00);

        seeker.addFunds(4999.99);

        assertThat(seeker.getBalance()).isEqualTo(19999.99);
    }

    /**
     * Verifies that a seeker's balance at the maximum capacity (20000) is accepted.
     */
    @Test
    @DisplayName("A resulting balance of exactly 20000 SEK is accepted")
    void balanceAtMaximumIsAccepted() {
        Seeker seeker = newValidSeeker();

        for (int topUp = 0; topUp < 4; topUp++) {
            seeker.addFunds(5000.00);
        }

        assertThat(seeker.getBalance()).isEqualTo(20000.00);
    }

    /**
     * Verifies that a seeker's balance cannot exceed the maximum capacity of 20000.
     */
    @Test
    @DisplayName("A top-up producing a 20000.01 SEK balance is rejected")
    void balanceJustAboveMaximumIsRejected() {
        Seeker seeker = newValidSeeker();
        seeker.addFunds(5000.00);
        seeker.addFunds(5000.00);
        seeker.addFunds(4990.01);
        seeker.addFunds(10.00); // balance is now 15000.01

        assertThrows(IllegalArgumentException.class, () -> seeker.addFunds(5000.00));
        assertThat(seeker.getBalance()).isEqualTo(15000.01);
    }

    // (Decision table): expected fee + max-bookings for each trust tier (FR-1.2).

    /**
     * Verifies that each {@link TrustTier} maps to its expected maximum concurrent bookings
     * and platform fee according to the expected decision table.
     *
     * @param tier - the trust tier being tested
     * @param maxBookings - the expected maximum concurrent bookings for the trust tier
     * @param platformFee - the expected platform fee for the trust tier
     */
    @ParameterizedTest(name = "{0}: max bookings {1}, platform fee {2}")
    @MethodSource("trustTierRules")
    @DisplayName("Each trust tier has the decision-table limits")
    void trustTierHasExpectedLimits(TrustTier tier, int maxBookings, double platformFee) {
        Seeker seeker = newValidSeeker();
        seeker.setTrustTier(tier);

        assertThat(seeker.getMaxConcurrentBookings()).isEqualTo(maxBookings);
        assertThat(seeker.getTrustTier().getPlatformFee()).isEqualTo(platformFee);
    }

    /**
     * Provides the expected decision table mappings for each {@link TrustTier} as a stream of arguments
     * (Maximum Concurrent Bookings & Platform fee).
     *
     * @return a stream of trust tier mappings as arguments.
     */
    private static Stream<Arguments> trustTierRules() {
        return Stream.of(
          Arguments.of(TrustTier.NEW, 1, 0.15),
          Arguments.of(TrustTier.VERIFIED, 3, 0.12),
          Arguments.of(TrustTier.TRUSTED, 5, 0.08),
          Arguments.of(TrustTier.PRO_SITTER, 10, 0.05)
        );
    }

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
}
