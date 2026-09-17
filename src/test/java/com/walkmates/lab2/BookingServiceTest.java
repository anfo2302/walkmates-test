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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

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

    @Test
    @DisplayName("Successful booking sends a confirmation notification")
    void successfulBookingSendsConfirmationNotification() {
        Seeker seeker = new Seeker("pat@example.com", "Pat", "0701112233");
        seeker.addFunds(500.00);

        Provider provider = new Provider("Animal owner", 62.39, 17.31);

        Listing listing = new Listing(
          provider.getId(),
          "Walk the dog",
          "A one-hour dog walk",
          ListingType.DOG_WALK);

        when(seekerRepository.findById("seeker-1")).thenReturn(Optional.of(seeker));
        when(listingRepository.findById(listing.getId())).thenReturn(Optional.of(listing));

        // The Seeker currently has no active bookings.
        when(bookingRepository.findBySeekerId("seeker-1")).thenReturn(List.of());

        when(providerRepository.findById(provider.getId())).thenReturn(Optional.of(provider));

        // The Provider currently has no active bookings.
        when(listingRepository.findByProviderId(provider.getId())).thenReturn(List.of(listing));
        when(bookingRepository.findByListingId(listing.getId())).thenReturn(List.of());

        // PricingCalculator is mocked so BookingService is tested in isolation.
        when(pricingCalculator.priceFor(
          any(Booking.class),
          same(listing),
          same(seeker))
        ).thenReturn(100.00);

        when(bookingRepository.save(any(Booking.class))).thenAnswer(
          invocation -> invocation.getArgument(0)
        );

        Booking result = bookingService.createBooking(
          "seeker-1",
          listing.getId(),
          60);

        assertThat(result.getStatus()).isEqualTo(BookingStatus.CONFIRMED);
        assertThat(result.getPrice()).isEqualTo(100.00);
        assertThat(seeker.getBalance()).isEqualTo(400.00);
        assertThat(listing.getStatus()).isEqualTo(ListingStatus.BOOKED);

        verify(seekerRepository).save(seeker);
        verify(listingRepository).save(listing);
        verify(bookingRepository).save(result);

        verify(notificationService).sendBookingConfirmed(seeker, result);
    }
}