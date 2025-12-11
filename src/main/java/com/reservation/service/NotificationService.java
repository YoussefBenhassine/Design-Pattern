package com.reservation.service;

import com.reservation.model.Notification;
import com.reservation.model.NotificationType;
import com.reservation.observer.NotificationObserver;
import com.reservation.observer.NotificationSubject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service de gestion des notifications
 * Design Pattern: Observer
 * Principe SOLID: SRP (Single Responsibility Principle) - Responsabilité unique de gestion des notifications
 * Principe SOLID: OCP (Open/Closed Principle) - On peut ajouter de nouveaux observateurs sans modifier le code
 */
public class NotificationService implements NotificationSubject {
    private final List<NotificationObserver> observers = new ArrayList<>();

    @Override
    public void attach(NotificationObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void detach(NotificationObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Notification notification) {
        for (NotificationObserver observer : observers) {
            observer.update(notification);
        }
    }

    /**
     * Envoie une notification
     */
    public Notification sendNotification(String userId, String message, NotificationType type) {
        Notification notification = new Notification(
            UUID.randomUUID().toString(),
            userId,
            message,
            type
        );

        notifyObservers(notification);
        return notification;
    }

    /**
     * Envoie une notification de réservation confirmée
     */
    public void sendReservationConfirmation(String userId, String reservationId) {
        String message = "Votre réservation " + reservationId + " a été confirmée.";
        sendNotification(userId, message, NotificationType.EMAIL);
        sendNotification(userId, message, NotificationType.IN_APP);
    }

    /**
     * Envoie une notification de paiement réussi
     */
    public void sendPaymentConfirmation(String userId, String paymentId) {
        String message = "Votre paiement " + paymentId + " a été traité avec succès.";
        sendNotification(userId, message, NotificationType.EMAIL);
        sendNotification(userId, message, NotificationType.SMS);
    }
}

