package com.reservation.service;

import com.reservation.model.Service;
import com.reservation.repository.ServiceRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service de recherche de services
 * Principe SOLID: SRP (Single Responsibility Principle) - Responsabilité unique de recherche
 */
public class ServiceSearchService {
    private final ServiceRepository serviceRepository;

    public ServiceSearchService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    /**
     * Recherche tous les services
     */
    public List<Service> searchAll() {
        return serviceRepository.findAll();
    }

    /**
     * Recherche par catégorie
     */
    public List<Service> searchByCategory(String category) {
        return serviceRepository.findByCategory(category);
    }

    /**
     * Recherche par prestataire
     */
    public List<Service> searchByPrestataire(String prestataireId) {
        return serviceRepository.findByPrestataireId(prestataireId);
    }

    /**
     * Recherche par nom (filtre)
     */
    public List<Service> searchByName(String name) {
        return serviceRepository.findAll().stream()
                .filter(service -> service.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Recherche combinée avec plusieurs filtres
     */
    public List<Service> search(String category, String prestataireId, String name) {
        List<Service> results = serviceRepository.findAll();

        if (category != null && !category.isEmpty()) {
            results = results.stream()
                    .filter(service -> service.getCategory().equals(category))
                    .collect(Collectors.toList());
        }

        if (prestataireId != null && !prestataireId.isEmpty()) {
            results = results.stream()
                    .filter(service -> service.getPrestataireId().equals(prestataireId))
                    .collect(Collectors.toList());
        }

        if (name != null && !name.isEmpty()) {
            results = results.stream()
                    .filter(service -> service.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }

        return results;
    }
}

