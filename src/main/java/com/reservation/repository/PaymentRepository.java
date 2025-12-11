package com.reservation.repository;

import com.reservation.model.Payment;
import com.reservation.storage.PaymentCsvStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implémentation du Repository pour les paiements
 */
public class PaymentRepository implements Repository<Payment, String> {
    private final Map<String, Payment> payments = new HashMap<>();
    private final PaymentCsvStorage csvStorage;

    public PaymentRepository() {
        this.csvStorage = new PaymentCsvStorage();
        loadFromCsv();
    }

    private void loadFromCsv() {
        List<Payment> loadedPayments = csvStorage.loadAll();
        for (Payment payment : loadedPayments) {
            payments.put(payment.getId(), payment);
        }
    }

    private void saveToCsv() {
        csvStorage.saveAll(payments.values().stream().collect(java.util.stream.Collectors.toList()));
    }

    @Override
    public Payment save(Payment payment) {
        payments.put(payment.getId(), payment);
        saveToCsv();
        return payment;
    }

    @Override
    public Optional<Payment> findById(String id) {
        return Optional.ofNullable(payments.get(id));
    }

    @Override
    public List<Payment> findAll() {
        return List.copyOf(payments.values());
    }

    @Override
    public void deleteById(String id) {
        payments.remove(id);
        saveToCsv();
    }

    @Override
    public boolean existsById(String id) {
        return payments.containsKey(id);
    }

    /**
     * Trouve le paiement d'une réservation
     */
    public Optional<Payment> findByReservationId(String reservationId) {
        return payments.values().stream()
                .filter(payment -> payment.getReservationId().equals(reservationId))
                .findFirst();
    }
}

