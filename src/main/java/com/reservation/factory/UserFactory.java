package com.reservation.factory;

import com.reservation.model.*;

/**
 * Factory Pattern pour créer des utilisateurs
 * Design Pattern: Factory
 * Principe SOLID: SRP (Single Responsibility Principle) - Responsabilité unique de création d'utilisateurs
 * Principe SOLID: OCP (Open/Closed Principle) - On peut ajouter de nouveaux types d'utilisateurs sans modifier le code existant
 */
public class UserFactory {
    
    /**
     * Crée un utilisateur selon son type
     * @param type Type d'utilisateur (CLIENT, PRESTATAIRE, ADMIN)
     * @param id Identifiant unique
     * @param name Nom
     * @param email Email
     * @param phone Téléphone
     * @return Instance de User
     */
    public static User createUser(UserType type, String id, String name, String email, String phone) {
        return switch (type) {
            case CLIENT -> new Client(id, name, email, phone);
            case PRESTATAIRE -> new Prestataire(id, name, email, phone);
            case ADMIN -> new Admin(id, name, email, phone);
        };
    }
}

