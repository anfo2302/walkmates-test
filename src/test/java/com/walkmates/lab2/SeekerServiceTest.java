package com.walkmates.lab2;

import com.walkmates.model.Seeker;
import com.walkmates.repository.SeekerRepository;
import com.walkmates.service.NotificationService;
import com.walkmates.service.PaymentService;
import com.walkmates.service.SeekerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SeekerServiceTest {

    @Mock
    private SeekerRepository seekerRepository;

    @Mock
    private PaymentService paymentService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private SeekerService seekerService;

    @Test
    @DisplayName("Successful payment credits the Seeker wallet")
    void successfulPaymentCreditsWallet()
            throws PaymentService.PaymentException {

        Seeker seeker =
                new Seeker("pat@example.com", "Pat", "0701112233");

        when(seekerRepository.findById("seeker-1"))
                .thenReturn(Optional.of(seeker));
        when(paymentService.charge("seeker-1", "payment-method-1", 250.00))
                .thenReturn("payment-confirmation-1");
        when(seekerRepository.save(any(Seeker.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Seeker updatedSeeker =
                seekerService.topUp(
                        "seeker-1",
                        "payment-method-1",
                        250.00);

        assertThat(updatedSeeker.getBalance()).isEqualTo(250.00);

        verify(paymentService)
                .charge("seeker-1", "payment-method-1", 250.00);
        verify(seekerRepository).save(seeker);
    }

    @Test
    @DisplayName("Declined payment does not credit the Seeker wallet")
    void declinedPaymentDoesNotCreditWallet()
            throws PaymentService.PaymentException {

        Seeker seeker =
                new Seeker("pat@example.com", "Pat", "0701112233");

        when(seekerRepository.findById("seeker-1"))
                .thenReturn(Optional.of(seeker));
        when(paymentService.charge("seeker-1", "payment-method-1", 250.00))
                .thenThrow(new PaymentService.PaymentException(
                        "Payment declined"));

        assertThatThrownBy(() ->
                seekerService.topUp(
                        "seeker-1",
                        "payment-method-1",
                        250.00))
                .isInstanceOf(PaymentService.PaymentException.class)
                .hasMessage("Payment declined");

        assertThat(seeker.getBalance()).isZero();
        verify(seekerRepository, never()).save(any(Seeker.class));
    }

    @Test
    @DisplayName("Payment timeout does not credit the Seeker wallet")
    void paymentTimeoutDoesNotCreditWallet()
            throws PaymentService.PaymentException {

        Seeker seeker =
                new Seeker("pat@example.com", "Pat", "0701112233");

        when(seekerRepository.findById("seeker-1"))
                .thenReturn(Optional.of(seeker));
        when(paymentService.charge("seeker-1", "payment-method-1", 250.00))
                .thenThrow(new PaymentService.PaymentTimeoutException(
                        "Payment gateway timed out"));

        assertThatThrownBy(() ->
                seekerService.topUp(
                        "seeker-1",
                        "payment-method-1",
                        250.00))
                .isInstanceOf(PaymentService.PaymentTimeoutException.class)
                .hasMessage("Payment gateway timed out");

        assertThat(seeker.getBalance()).isZero();
        verify(seekerRepository, never()).save(any(Seeker.class));
    }
}