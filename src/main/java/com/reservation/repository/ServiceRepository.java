package com.reservation.repository;

import com.reservation.model.Service;
import com.reservation.storage.ServiceCsvStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implémentation du Repository pour les services
 */
public class ServiceRepository implements Repository<Service, String> {
    private final Map<String, Service> services = new HashMap<>();
    private final ServiceCsvStorage csvStorage;

    public ServiceRepository() {
        this.csvStorage = new ServiceCsvStorage();
        loadFromCsv();
    }

    private void loadFromCsv() {
        List<Service> loadedServices = csvStorage.loadAll();
        for (Service service : loadedServices) {
            services.put(service.getId(), service);
        }
    }

    private void saveToCsv() {
        csvStorage.saveAll(services.values().stream().collect(Collectors.toList()));
    }

    @Override
    public Service save(Service service) {
        services.put(service.getId(), service);
        saveToCsv();
        return service;
    }

    @Override
    public Optional<Service> findById(String id) {
        return Optional.ofNullable(services.get(id));
    }

    @Override
    public List<Service> findAll() {
        return List.copyOf(services.values());
    }

    @Override
    public void deleteById(String id) {
        services.remove(id);
        saveToCsv();
    }

    @Override
    public boolean existsById(String id) {
        return services.containsKey(id);
    }

    /**
     * Trouve les services par catégorie
     */
    public List<Service> findByCategory(String category) {
        return services.values().stream()
                .filter(service -> service.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    /**
     * Trouve les services d'un prestataire
     */
    public List<Service> findByPrestataireId(String prestataireId) {
        return services.values().stream()
                .filter(service -> service.getPrestataireId().equals(prestataireId))
                .collect(Collectors.toList());
    }
}

