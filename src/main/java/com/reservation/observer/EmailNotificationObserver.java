package com.reservation.observer;

import com.reservation.model.Notification;
import com.reservation.model.NotificationType;

/**
 * Observateur pour les notifications email
 */
public class EmailNotificationObserver implements NotificationObserver {
    @Override
    public void update(Notification notification) {
        if (notification.getType() == NotificationType.EMAIL) {
            System.out.println("📧 Email envoyé à " + notification.getUserId() + ": " + notification.getMessage());
        }
    }
}

