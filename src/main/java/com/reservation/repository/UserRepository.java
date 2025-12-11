package com.reservation.repository;

import com.reservation.model.User;
import com.reservation.storage.UserCsvStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implémentation du Repository pour les utilisateurs
 * Design Pattern: Repository
 * Principe SOLID: SRP (Single Responsibility Principle) - Responsabilité unique de gestion des utilisateurs
 */
public class UserRepository implements Repository<User, String> {
    private final Map<String, User> users = new HashMap<>();
    private final UserCsvStorage csvStorage;

    public UserRepository() {
        this.csvStorage = new UserCsvStorage();
        loadFromCsv();
    }

    private void loadFromCsv() {
        List<User> loadedUsers = csvStorage.loadAll();
        for (User user : loadedUsers) {
            users.put(user.getId(), user);
        }
    }

    private void saveToCsv() {
        csvStorage.saveAll(users.values().stream().collect(Collectors.toList()));
    }

    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        saveToCsv();
        return user;
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(users.values());
    }

    @Override
    public void deleteById(String id) {
        users.remove(id);
        saveToCsv();
    }

    @Override
    public boolean existsById(String id) {
        return users.containsKey(id);
    }

    /**
     * Trouve un utilisateur par email
     */
    public Optional<User> findByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }
}

