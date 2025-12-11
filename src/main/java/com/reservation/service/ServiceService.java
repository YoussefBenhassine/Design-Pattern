package com.reservation.service;

import com.reservation.model.Service;
import com.reservation.repository.ServiceRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service de gestion des services
 * Principe SOLID: SRP (Single Responsibility Principle) - Responsabilité unique de gestion des services
 * Principe SOLID: DIP (Dependency Inversion Principle) - Dépend de l'abstraction Repository
 */
public class ServiceService {
    private final ServiceRepository serviceRepository;

    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    /**
     * Crée un nouveau service
     */
    public Service createService(String id, String name, String description, 
                                String category, BigDecimal price, 
                                int duration, String prestataireId) {
        if (serviceRepository.existsById(id)) {
            throw new IllegalArgumentException("Un service avec l'ID " + id + " existe déjà");
        }

        Service service = new Service(id, name, description, category, price, duration, prestataireId);
        return serviceRepository.save(service);
    }

    /**
     * Met à jour un service existant
     */
    public Service updateService(String id, String name, String description,
                                String category, BigDecimal price,
                                int duration, String prestataireId) {
        Optional<Service> existingService = serviceRepository.findById(id);
        if (existingService.isEmpty()) {
            throw new IllegalArgumentException("Service introuvable: " + id);
        }

        Service updatedService = new Service(id, name, description, category, price, duration, prestataireId);
        return serviceRepository.save(updatedService);
    }

    /**
     * Supprime un service
     */
    public void deleteService(String id) {
        if (!serviceRepository.existsById(id)) {
            throw new IllegalArgumentException("Service introuvable: " + id);
        }
        serviceRepository.deleteById(id);
    }

    /**
     * Récupère un service par ID
     */
    public Optional<Service> getServiceById(String id) {
        return serviceRepository.findById(id);
    }

    /**
     * Récupère tous les services
     */
    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    /**
     * Récupère les services d'un prestataire
     */
    public List<Service> getServicesByPrestataire(String prestataireId) {
        return serviceRepository.findByPrestataireId(prestataireId);
    }

    /**
     * Récupère les services par catégorie
     */
    public List<Service> getServicesByCategory(String category) {
        return serviceRepository.findByCategory(category);
    }
}

