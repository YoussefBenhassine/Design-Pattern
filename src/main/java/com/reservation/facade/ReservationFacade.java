package com.reservation.facade;

import com.reservation.model.*;
import com.reservation.payment.PaymentStrategy;
import com.reservation.payment.CreditCardPaymentStrategy;
import com.reservation.payment.PayPalPaymentStrategy;
import com.reservation.payment.WalletPaymentStrategy;
import com.reservation.repository.PaymentRepository;
import com.reservation.service.NotificationService;
import com.reservation.service.PaymentService;
import com.reservation.service.ReservationService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Facade Pattern pour simplifier l'interface de réservation
 * Design Pattern: Facade
 * Principe SOLID: SRP (Single Responsibility Principle) - Responsabilité unique de simplification de l'interface
 * Principe SOLID: DIP (Dependency Inversion Principle) - Dépend des abstractions
 * 
 * Cette classe simplifie l'utilisation des services complexes de réservation, paiement et notification
 */
public class ReservationFacade {
    private final ReservationService reservationService;
    private final PaymentService paymentService;
    private final NotificationService notificationService;

    public ReservationFacade(ReservationService reservationService,
                            PaymentService paymentService,
                            NotificationService notificationService) {
        this.reservationService = reservationService;
        this.paymentService = paymentService;
        this.notificationService = notificationService;
    }

    /**
     * Effectue une réservation complète avec paiement
     * @param clientId ID du client
     * @param serviceId ID du service
     * @param prestataireId ID du prestataire
     * @param dateTime Date et heure de la réservation
     * @param paymentMethod Méthode de paiement
     * @param paymentDetails Détails du paiement
     * @return La réservation créée
     */
    public Reservation completeReservation(String clientId, String serviceId, 
                                          String prestataireId, LocalDateTime dateTime,
                                          PaymentMethod paymentMethod, String paymentDetails) {
        // 1. Créer la réservation
        Reservation reservation = reservationService.createReservation(
            clientId, serviceId, prestataireId, dateTime
        );

        // 2. Configurer la stratégie de paiement
        PaymentStrategy strategy = getPaymentStrategy(paymentMethod);
        paymentService.setPaymentStrategy(strategy);

        // 3. Traiter le paiement
        Payment payment = paymentService.processPayment(
            reservation.getId(),
            reservation.getTotalAmount(),
            paymentMethod,
            paymentDetails
        );

        // 4. Si le paiement échoue, annuler la réservation
        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            reservationService.cancelReservation(reservation.getId());
            throw new RuntimeException("Le paiement a échoué. La réservation a été annulée.");
        }

        // 5. Envoyer la confirmation de paiement
        notificationService.sendPaymentConfirmation(clientId, payment.getId());

        return reservation;
    }

    /**
     * Annule une réservation et rembourse le paiement
     */
    public void cancelReservationWithRefund(String reservationId) {
        // 1. Annuler la réservation
        reservationService.cancelReservation(reservationId);

        // 2. Rembourser le paiement
        PaymentRepository paymentRepo = paymentService.getPaymentRepository();
        paymentRepo.findByReservationId(reservationId).ifPresent(payment -> {
            paymentService.refundPayment(payment.getId());
        });
    }

    /**
     * Obtient la stratégie de paiement appropriée
     * Design Pattern: Strategy
     */
    private PaymentStrategy getPaymentStrategy(PaymentMethod method) {
        return switch (method) {
            case CREDIT_CARD -> new CreditCardPaymentStrategy();
            case PAYPAL -> new PayPalPaymentStrategy();
            case WALLET -> new WalletPaymentStrategy();
        };
    }
}

