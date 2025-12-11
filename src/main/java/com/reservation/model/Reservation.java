package com.reservation.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Représente une réservation
 */
public class Reservation {
    private String id;
    private String clientId;
    private String serviceId;
    private String prestataireId;
    private LocalDateTime dateTime;
    private ReservationStatus status;
    private BigDecimal totalAmount;

    public Reservation(String id, String clientId, String serviceId, 
                      String prestataireId, LocalDateTime dateTime, BigDecimal totalAmount) {
        this.id = id;
        this.clientId = clientId;
        this.serviceId = serviceId;
        this.prestataireId = prestataireId;
        this.dateTime = dateTime;
        this.status = ReservationStatus.PENDING;
        this.totalAmount = totalAmount;
    }

    public String getId() {
        return id;
    }

    public String getClientId() {
        return clientId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getPrestataireId() {
        return prestataireId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void cancel() {
        if (this.status == ReservationStatus.CONFIRMED || this.status == ReservationStatus.PENDING) {
            this.status = ReservationStatus.CANCELLED;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

