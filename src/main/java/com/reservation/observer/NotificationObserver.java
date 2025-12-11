package com.reservation.observer;

import com.reservation.model.Notification;

/**
 * Interface Observer pour les notifications
 * Design Pattern: Observer
 * Principe SOLID: OCP (Open/Closed Principle) - On peut ajouter de nouveaux observateurs sans modifier le sujet
 */
public interface NotificationObserver {
    /**
     * Méthode appelée lorsqu'une notification est envoyée
     * @param notification La notification envoyée
     */
    void update(Notification notification);
}

