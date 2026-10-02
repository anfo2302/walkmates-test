package com.walkmates.lab2;

import com.walkmates.model.Booking;
import com.walkmates.model.BookingStatus;
import com.walkmates.model.Listing;
import com.walkmates.model.ListingStatus;
import com.walkmates.model.ListingType;
import com.walkmates.model.Provider;
import com.walkmates.model.Seeker;
import com.walkmates.repository.BookingRepository;
import com.walkmates.repository.ListingRepository;
import com.walkmates.repository.ProviderRepository;
import com.walkmates.repository.SeekerRepository;
import com.walkmates.service.BookingService;
import com.walkmates.service.NotificationService;
import com.walkmates.service.PricingCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link BookingService}, covering the booking creation workflow defined by:
 * {@link BookingService#createBooking(String, String, int)}.
 *
 * <p>
 *   The success path test verifies that a booking is created end-to-end
 *   with the current side effects across the collaborating components.
 * </p>
 *
 * <p>
 *   Dependencies ({@link SeekerRepository}, {@link ListingRepository},
 *   {@link ProviderRepository}, {@link BookingRepository}, {@link PricingCalculator},
 *   and {@link NotificationService}) are mocked via Mockito's
 *   {@link org.mockito.junit.jupiter.MockitoExtension},
 *   so {@link BookingService} is tested in isolation from persistence,
 *   pricing, and notification infrastructure.
 *   {@link PricingCalculator} is stubbed to return a fixed price,
 *   decoupling these tests from the pricing rules.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    private static final String SEEKER_ID = "seeker-1";
    private static final int VALID_DURATION_MINUTES = 60;
    private static final double BOOKING_PRICE = 100.00;

    @Mock
    private SeekerRepository seekerRepository;

    @Mock
    private ListingRepository listingRepository;

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private PricingCalculator pricingCalculator;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private BookingService bookingService;

    private Seeker seeker;
    private Provider provider;
    private Listing listing;

    /**
     * Creates reusable domain objects used by all booking scenarios.
     *
     * <p>
     *   Establishes a funded {@link Seeker}, a {@link Provider},
     *   and an available {@link Listing} so individual tests can focus
     *   on behaviour rather than object construction.
     * </p>
     */
    @BeforeEach
    void setup() {
        this.seeker = new Seeker("pat@example.com", "Pat", "0701112233");
        this.seeker.addFunds(500.00);

        this.provider = new Provider("Animal owner", 62.39, 17.31);

        this.listing = new Listing(
          this.provider.getId(),
          "Walk the dog",
          "A one-hour dog walk",
          ListingType.DOG_WALK);
    }

    /**
     * Configures mocked collaborators for a successful booking scenario.
     *
     * <p>
     *   The seeker, provider, and listing exists, there are no conflicting bookings,
     *   and the pricing service returns a fixed booking price.
     * </p>
     */
    private void stubSuccessfulBookingFlow() {
        when(seekerRepository.findById(SEEKER_ID))
          .thenReturn(Optional.of(seeker));

        when(listingRepository.findById(listing.getId()))
          .thenReturn(Optional.of(listing));

        when(providerRepository.findById(provider.getId()))
          .thenReturn(Optional.of(provider));

        when(bookingRepository.findBySeekerId(SEEKER_ID))
          .thenReturn(List.of());

        when(listingRepository.findByProviderId(provider.getId()))
          .thenReturn(List.of(listing));

        when(bookingRepository.findByListingId(listing.getId()))
          .thenReturn(List.of());

        when(pricingCalculator.priceFor(
          any(Booking.class),
          same(listing),
          same(seeker))
        ).thenReturn(BOOKING_PRICE);

        when(bookingRepository.save(any(Booking.class)))
          .thenAnswer(invocation -> invocation.getArgument(0)
        );
    }

    /**
     * Executes a valid booking request using the shared happy-path setup.
     * @return the created booking
     */
    private Booking createSuccessfulBooking() {
        stubSuccessfulBookingFlow();

        return bookingService.createBooking(
          SEEKER_ID,
          listing.getId(),
          VALID_DURATION_MINUTES
        );
    }

    /**
     * Verifies the successful execution of {@link BookingService#createBooking(String, String, int)}.
     *
     * <p>A valid booking request should complete the entire booking workflow:</p>
     * <ul>
     *   <li>Create a booking with {@link BookingStatus#CONFIRMED} status.</li>
     *   <li>Store the price returned by {@link PricingCalculator}.</li>
     *   <li>Charge the {@link Seeker}'s wallet.</li>
     *   <li>Mark the {@link Listing} as {@link ListingStatus#BOOKED}.</li>
     *   <li>Persist all modified entities.</li>
     *   <li>Send a booking confirmation notification.</li>
     * </ul>
     *
     * <p>
     *   Assertions are grouped using {@code assertAll(...)}
     *   so that all workflow failures are reported together
     *   rather than stopping at the first failing assertion.
     * </p>
     */
    @Test
    @DisplayName("Successful booking completes the entire booking workflow")
    void successfulBookingCompletesWorkflow() {
        Booking booking = createSuccessfulBooking();

        assertAll(
          "successful booking workflow",

          () -> assertThat(booking.getStatus())
            .as("booking status should be confirmed")
            .isEqualTo(BookingStatus.CONFIRMED),

          () -> assertThat(booking.getPrice())
            .as("booking price should match the calculated price")
            .isEqualTo(BOOKING_PRICE),

          () -> assertThat(seeker.getBalance())
            .as("seeker wallet should be charged by the booking price")
            .isEqualTo(400.00),

          () -> assertThat(listing.getStatus())
            .as("listing status should be booked after booking")
            .isEqualTo(ListingStatus.BOOKED)
        );

        assertAll(
          "Seeker, booking, and listing entities should be persisted after a successful booking",
          () -> verify(seekerRepository).save(seeker),
          () -> verify(listingRepository).save(listing),
          () -> verify(bookingRepository).save(booking)
        );

        assertAll(
          "Booking confirmation notification should be sent",
          () -> verify(notificationService).sendBookingConfirmed(seeker, booking)
        );
    }
}