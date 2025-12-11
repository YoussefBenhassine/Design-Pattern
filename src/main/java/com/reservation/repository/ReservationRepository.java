package com.reservation.repository;

import com.reservation.model.Reservation;
import com.reservation.model.ReservationStatus;
import com.reservation.storage.ReservationCsvStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implémentation du Repository pour les réservations
 */
public class ReservationRepository implements Repository<Reservation, String> {
    private final Map<String, Reservation> reservations = new HashMap<>();
    private final ReservationCsvStorage csvStorage;

    public ReservationRepository() {
        this.csvStorage = new ReservationCsvStorage();
        loadFromCsv();
    }

    private void loadFromCsv() {
        List<Reservation> loadedReservations = csvStorage.loadAll();
        for (Reservation reservation : loadedReservations) {
            reservations.put(reservation.getId(), reservation);
        }
    }

    private void saveToCsv() {
        csvStorage.saveAll(reservations.values().stream().collect(Collectors.toList()));
    }

    @Override
    public Reservation save(Reservation reservation) {
        reservations.put(reservation.getId(), reservation);
        saveToCsv();
        return reservation;
    }

    @Override
    public Optional<Reservation> findById(String id) {
        return Optional.ofNullable(reservations.get(id));
    }

    @Override
    public List<Reservation> findAll() {
        return List.copyOf(reservations.values());
    }

    @Override
    public void deleteById(String id) {
        reservations.remove(id);
        saveToCsv();
    }

    @Override
    public boolean existsById(String id) {
        return reservations.containsKey(id);
    }

    /**
     * Trouve les réservations d'un client
     */
    public List<Reservation> findByClientId(String clientId) {
        return reservations.values().stream()
                .filter(reservation -> reservation.getClientId().equals(clientId))
                .collect(Collectors.toList());
    }

    /**
     * Trouve les réservations d'un prestataire
     */
    public List<Reservation> findByPrestataireId(String prestataireId) {
        return reservations.values().stream()
                .filter(reservation -> reservation.getPrestataireId().equals(prestataireId))
                .collect(Collectors.toList());
    }

    /**
     * Trouve les réservations par statut
     */
    public List<Reservation> findByStatus(ReservationStatus status) {
        return reservations.values().stream()
                .filter(reservation -> reservation.getStatus() == status)
                .collect(Collectors.toList());
    }
}

