package com.reservation.observer;

import com.reservation.model.Notification;

/**
 * Interface Subject pour le pattern Observer
 * Design Pattern: Observer
 */
public interface NotificationSubject {
    /**
     * Ajoute un observateur
     */
    void attach(NotificationObserver observer);
    
    /**
     * Retire un observateur
     */
    void detach(NotificationObserver observer);
    
    /**
     * Notifie tous les observateurs
     */
    void notifyObservers(Notification notification);
}

