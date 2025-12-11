package com.reservation.builder;

import com.reservation.model.Reservation;
import com.reservation.model.ReservationStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Builder Pattern pour construire des réservations
 * Design Pattern: Builder
 * Principe SOLID: SRP (Single Responsibility Principle) - Responsabilité unique de construction d'objets complexes
 * Principe SOLID: OCP (Open/Closed Principle) - Facile d'ajouter de nouveaux paramètres sans modifier le constructeur
 */
public class ReservationBuilder {
    private String id;
    private String clientId;
    private String serviceId;
    private String prestataireId;
    private LocalDateTime dateTime;
    private BigDecimal totalAmount;

    public ReservationBuilder() {
        this.id = UUID.randomUUID().toString();
    }

    public ReservationBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public ReservationBuilder withClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public ReservationBuilder withServiceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public ReservationBuilder withPrestataireId(String prestataireId) {
        this.prestataireId = prestataireId;
        return this;
    }

    public ReservationBuilder withDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public ReservationBuilder withTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    /**
     * Construit la réservation
     * @return Instance de Reservation
     * @throws IllegalStateException si les paramètres requis sont manquants
     */
    public Reservation build() {
        if (clientId == null || serviceId == null || prestataireId == null || 
            dateTime == null || totalAmount == null) {
            throw new IllegalStateException("Tous les paramètres requis doivent être fournis");
        }
        return new Reservation(id, clientId, serviceId, prestataireId, dateTime, totalAmount);
    }
}

