package com.reservation.repository;

import java.util.List;
import java.util.Optional;

/**
 * Interface générique pour les repositories
 * Design Pattern: Repository
 * Principe SOLID: DIP (Dependency Inversion Principle) - Dépendance sur l'abstraction, pas l'implémentation
 * @param <T> Type de l'entité
 * @param <ID> Type de l'identifiant
 */
public interface Repository<T, ID> {
    /**
     * Sauvegarde une entité
     */
    T save(T entity);
    
    /**
     * Trouve une entité par son ID
     */
    Optional<T> findById(ID id);
    
    /**
     * Trouve toutes les entités
     */
    List<T> findAll();
    
    /**
     * Supprime une entité
     */
    void deleteById(ID id);
    
    /**
     * Vérifie si une entité existe
     */
    boolean existsById(ID id);
}

