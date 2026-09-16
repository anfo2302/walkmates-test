package com.walkmates.lab2;

import com.walkmates.model.Booking;
import com.walkmates.model.Listing;
import com.walkmates.model.ListingType;
import com.walkmates.model.Seeker;
import com.walkmates.model.TrustTier;
import com.walkmates.service.PricingCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Lab 2, Part A — structural testing for {@link PricingCalculator} (FR-4.3).
 *
 * <p>Run coverage with {@code mvn clean test jacoco:report} and open
 * {@code target/site/jacoco/index.html}. Find the uncovered branches and add tests to reach
 * them — then look hard at the <em>overnight surcharge boundary</em>: there is a path that your
 * happy-path test "covers" but does not actually check (coverage ≠ correctness).</p>
 */
class PricingCalculatorStructuralTest {

    private final PricingCalculator pricing = new PricingCalculator();

    private Seeker seeker(TrustTier tier) {
        Seeker s = new Seeker("p@example.com", "Pat", "0701112233");
        s.setTrustTier(tier);
        return s;
    }

    private Listing listing(ListingType type) {
        return new Listing("provider-1", "A listing", "desc", type);
    }

    // ---- Worked example: a short standard walk, no overnight surcharge ----
    @Test
    @DisplayName("60 min DOG_WALK for a VERIFIED seeker = 80 base + 12% fee = 89.60")
    void shortWalkPrice() {
        Booking booking = new Booking("seeker-1", "listing-1", 60);

        double price = pricing.priceFor(booking, listing(ListingType.DOG_WALK), seeker(TrustTier.VERIFIED));

        assertThat(price).isEqualTo(89.60);
    }

    // (branch): a free SHELTER_VOLUNTEER listing always costs 0.00.

    /**
     * Verifies that the free-listing branch for {@link ListingType#SHELTER_VOLUNTEER}
     * bypasses all pricing logic (overnight surcharge and trust tier platform fee)
     * and always returns 0.00.
     *
     * <p>
     *   A duration above the overnight threshold is used to confirm that
     *   the surcharge is not applied when the listing type is {@link ListingType#SHELTER_VOLUNTEER}.
     * </p>
     */
    @Test
    @DisplayName("A SHELTER_VOLUNTEER booking is free")
    void shelterVolunteerListingIsFree() {
        Booking booking = new Booking("seeker-1", "listing-1", 600);

        double price = pricing.priceFor(
                booking,
                listing(ListingType.SHELTER_VOLUNTEER),
                seeker(TrustTier.VERIFIED));

        assertThat(price).isEqualTo(0.00);
    }

    // (branch): a clearly-overnight booking (e.g. 600 min) includes the 20% surcharge.

    /**
     * Verifies that bookings strictly greater than 480 minutes
     * add a 20% surcharge before the platform fee is applied.
     *
     * <p><i>This test uses a clearly overnight duration of 600 minutes to exercise the branch.</i></p>
     */
    @Test
    @DisplayName("600 min DOG_WALK includes the 20% overnight surcharge")
    void overnightBookingIncludesSurcharge() {
        Booking booking = new Booking("seeker-1", "listing-1", 600);

        double price = pricing.priceFor(
                booking,
                listing(ListingType.DOG_WALK),
                seeker(TrustTier.VERIFIED));

        // 10 hours × 80 = 800
        // 20% overnight surcharge = 160
        // Subtotal = 960
        // 12% platform fee = 115.20
        // Total = 1075.20
        assertThat(price).isEqualTo(1075.20);
    }

    // (BOUNDARY — this is the interesting one): a booking of exactly 480 minutes must NOT
    //      be surcharged (FR-4.3 says strictly > 480). Write this test and see what happens.

    /**
     * Verifies that a booking of <i>exactly 480 minutes</i> is not charged the 20% overnight surcharge.
     */
    @Test
    @DisplayName("Exactly 480 min does not include the overnight surcharge")
    void bookingAtOvernightBoundaryDoesNotIncludeSurcharge() {
        Booking booking = new Booking("seeker-1", "listing-1", 480);

        double price = pricing.priceFor(
                booking,
                listing(ListingType.DOG_WALK),
                seeker(TrustTier.VERIFIED));

        // 8 hours × 80 = 640
        // No overnight surcharge
        // 12% platform fee = 76.80
        // Total = 716.80
        assertThat(price).isEqualTo(716.80);
    }

    // (Exception): null booking, listing, or seeker throws IllegalArgumentException

    /**
     * Verifies that a {@code null} {@link Booking} causes a {@link IllegalArgumentException}.
     */
    @Test
    @DisplayName("A null booking is rejected")
    void nullBookingIsRejected() {
        Listing listing = listing(ListingType.DOG_WALK);
        Seeker seeker = seeker(TrustTier.VERIFIED);

        assertThatThrownBy(() -> pricing.priceFor(null, listing, seeker))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Booking, listing and seeker are all required");
    }

    /**
     * Verifies that a {@code null} {@link Listing} causes a {@link IllegalArgumentException}.
     */
    @Test
    @DisplayName("A null listing is rejected")
    void nullListingIsRejected() {
        Booking booking = new Booking("seeker-1", "listing-1", 60);
        Seeker seeker = seeker(TrustTier.VERIFIED);

        assertThatThrownBy(() -> pricing.priceFor(booking, null, seeker))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Booking, listing and seeker are all required");
    }

    /**
     * Verifies that a {@code null} {@link Seeker} causes a {@link IllegalArgumentException}.
     */
    @Test
    @DisplayName("A null seeker is rejected")
    void nullSeekerIsRejected() {
        Booking booking = new Booking("seeker-1", "listing-1", 60);
        Listing listing = listing(ListingType.DOG_WALK);

        assertThatThrownBy(() -> pricing.priceFor(booking, listing, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Booking, listing and seeker are all required");
    }
}
