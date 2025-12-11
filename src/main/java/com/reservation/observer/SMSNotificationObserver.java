package com.reservation.observer;

import com.reservation.model.Notification;
import com.reservation.model.NotificationType;

/**
 * Observateur pour les notifications SMS
 */
public class SMSNotificationObserver implements NotificationObserver {
    @Override
    public void update(Notification notification) {
        if (notification.getType() == NotificationType.SMS) {
            System.out.println("📱 SMS envoyé à " + notification.getUserId() + ": " + notification.getMessage());
        }
    }
}

