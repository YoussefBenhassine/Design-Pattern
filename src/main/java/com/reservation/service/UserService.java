package com.reservation.service;

import com.reservation.factory.UserFactory;
import com.reservation.factory.UserType;
import com.reservation.model.User;
import com.reservation.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service de gestion des utilisateurs
 * Principe SOLID: SRP (Single Responsibility Principle) - Responsabilité unique de gestion des utilisateurs
 * Design Pattern: Factory (utilisé via UserFactory)
 */
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Crée un utilisateur
     * Design Pattern: Factory
     */
    public User createUser(UserType type, String id, String name, String email, String phone) {
        User user = UserFactory.createUser(type, id, name, email, phone);
        return userRepository.save(user);
    }

    /**
     * Trouve un utilisateur par ID
     */
    public Optional<User> findUserById(String id) {
        return userRepository.findById(id);
    }

    /**
     * Trouve un utilisateur par email
     */
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Récupère tous les utilisateurs
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Supprime un utilisateur
     */
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}

