package com.reservation.storage;

import com.reservation.model.Reservation;
import com.reservation.model.ReservationStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Service de stockage CSV pour les réservations
 */
public class ReservationCsvStorage {
    private static final String CSV_FILE = "reservations.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final CsvStorageService csvService;

    public ReservationCsvStorage() {
        this.csvService = new CsvStorageService();
        initializeCsvFile();
    }

    private void initializeCsvFile() {
        if (!csvService.fileExists(CSV_FILE)) {
            List<String[]> header = new ArrayList<>();
            header.add(new String[]{"id", "clientId", "serviceId", "prestataireId", "dateTime", "status", "totalAmount"});
            csvService.writeCsv(CSV_FILE, header);
        }
    }

    /**
     * Charge toutes les réservations depuis le CSV
     */
    public List<Reservation> loadAll() {
        List<Reservation> reservations = new ArrayList<>();
        List<String[]> rows = csvService.readCsv(CSV_FILE);
        
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row.length >= 7) {
                try {
                    String id = row[0];
                    String clientId = row[1];
                    String serviceId = row[2];
                    String prestataireId = row[3];
                    LocalDateTime dateTime = LocalDateTime.parse(row[4], DATE_FORMATTER);
                    ReservationStatus status = ReservationStatus.valueOf(row[5]);
                    BigDecimal totalAmount = new BigDecimal(row[6]);
                    
                    Reservation reservation = new Reservation(id, clientId, serviceId, prestataireId, dateTime, totalAmount);
                    reservation.setStatus(status);
                    reservations.add(reservation);
                } catch (Exception e) {
                    System.err.println("Erreur lors du chargement de la réservation: " + e.getMessage());
                }
            }
        }
        
        return reservations;
    }

    /**
     * Sauvegarde toutes les réservations dans le CSV
     */
    public void saveAll(List<Reservation> reservations) {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"id", "clientId", "serviceId", "prestataireId", "dateTime", "status", "totalAmount"});
        
        for (Reservation reservation : reservations) {
            rows.add(new String[]{
                reservation.getId(),
                reservation.getClientId(),
                reservation.getServiceId(),
                reservation.getPrestataireId(),
                reservation.getDateTime().format(DATE_FORMATTER),
                reservation.getStatus().name(),
                reservation.getTotalAmount().toString()
            });
        }
        
        csvService.writeCsv(CSV_FILE, rows);
    }
}

