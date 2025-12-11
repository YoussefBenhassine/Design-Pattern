package com.reservation.service;

import com.reservation.builder.ReservationBuilder;
import com.reservation.model.*;
import com.reservation.repository.ReservationRepository;
import com.reservation.repository.ServiceRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service de gestion des réservations
 * Principe SOLID: SRP (Single Responsibility Principle) - Responsabilité unique de gestion des réservations
 * Principe SOLID: DIP (Dependency Inversion Principle) - Dépend des abstractions (repositories)
 */
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ServiceRepository serviceRepository;
    private final NotificationService notificationService;

    public ReservationService(ReservationRepository reservationRepository,
                            ServiceRepository serviceRepository,
                            NotificationService notificationService) {
        this.reservationRepository = reservationRepository;
        this.serviceRepository = serviceRepository;
        this.notificationService = notificationService;
    }

    /**
     * Crée une réservation
     * Design Pattern: Builder
     */
    public Reservation createReservation(String clientId, String serviceId, 
                                        String prestataireId, LocalDateTime dateTime) {
        Optional<Service> serviceOpt = serviceRepository.findById(serviceId);
        if (serviceOpt.isEmpty()) {
            throw new IllegalArgumentException("Service introuvable: " + serviceId);
        }

        Service service = serviceOpt.get();
        Reservation reservation = new ReservationBuilder()
            .withClientId(clientId)
            .withServiceId(serviceId)
            .withPrestataireId(prestataireId)
            .withDateTime(dateTime)
            .withTotalAmount(service.getPrice())
            .build();

        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation = reservationRepository.save(reservation);

        // Envoi de notification
        notificationService.sendReservationConfirmation(clientId, reservation.getId());
        notificationService.sendReservationConfirmation(prestataireId, reservation.getId());

        return reservation;
    }

    /**
     * Annule une réservation
     */
    public void cancelReservation(String reservationId) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);
        if (reservationOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();
            reservation.cancel();
            reservationRepository.save(reservation);

            // Notification
            notificationService.sendNotification(
                reservation.getClientId(),
                "Votre réservation " + reservationId + " a été annulée.",
                NotificationType.EMAIL
            );
        }
    }

    /**
     * Modifie une réservation
     */
    public Reservation updateReservation(String reservationId, LocalDateTime newDateTime) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);
        if (reservationOpt.isEmpty()) {
            throw new IllegalArgumentException("Réservation introuvable: " + reservationId);
        }

        Reservation reservation = reservationOpt.get();
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("Impossible de modifier une réservation annulée");
        }

        // Créer une nouvelle réservation avec les nouvelles dates
        Reservation updatedReservation = new ReservationBuilder()
            .withId(reservation.getId())
            .withClientId(reservation.getClientId())
            .withServiceId(reservation.getServiceId())
            .withPrestataireId(reservation.getPrestataireId())
            .withDateTime(newDateTime)
            .withTotalAmount(reservation.getTotalAmount())
            .build();
        
        updatedReservation.setStatus(reservation.getStatus());
        return reservationRepository.save(updatedReservation);
    }

    /**
     * Récupère l'historique des réservations d'un client
     */
    public List<Reservation> getClientHistory(String clientId) {
        return reservationRepository.findByClientId(clientId);
    }

    /**
     * Récupère une réservation par ID
     */
    public Optional<Reservation> getReservationById(String reservationId) {
        return reservationRepository.findById(reservationId);
    }
}

