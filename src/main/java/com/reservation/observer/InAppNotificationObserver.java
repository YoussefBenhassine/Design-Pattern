package com.reservation.observer;

import com.reservation.model.Notification;
import com.reservation.model.NotificationType;

/**
 * Observateur pour les notifications in-app
 */
public class InAppNotificationObserver implements NotificationObserver {
    @Override
    public void update(Notification notification) {
        if (notification.getType() == NotificationType.IN_APP) {
            System.out.println("🔔 Notification in-app pour " + notification.getUserId() + ": " + notification.getMessage());
        }
    }
}

