package com.reservation.storage;

import com.reservation.model.Payment;
import com.reservation.model.PaymentMethod;
import com.reservation.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Service de stockage CSV pour les paiements
 */
public class PaymentCsvStorage {
    private static final String CSV_FILE = "payments.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final CsvStorageService csvService;

    public PaymentCsvStorage() {
        this.csvService = new CsvStorageService();
        initializeCsvFile();
    }

    private void initializeCsvFile() {
        if (!csvService.fileExists(CSV_FILE)) {
            List<String[]> header = new ArrayList<>();
            header.add(new String[]{"id", "reservationId", "amount", "method", "status", "paymentDate"});
            csvService.writeCsv(CSV_FILE, header);
        }
    }

    /**
     * Charge tous les paiements depuis le CSV
     */
    public List<Payment> loadAll() {
        List<Payment> payments = new ArrayList<>();
        List<String[]> rows = csvService.readCsv(CSV_FILE);
        
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row.length >= 6) {
                try {
                    String id = row[0];
                    String reservationId = row[1];
                    BigDecimal amount = new BigDecimal(row[2]);
                    PaymentMethod method = PaymentMethod.valueOf(row[3]);
                    PaymentStatus status = PaymentStatus.valueOf(row[4]);
                    LocalDateTime paymentDate = row[5].isEmpty() ? null : LocalDateTime.parse(row[5], DATE_FORMATTER);
                    
                    Payment payment = new Payment(id, reservationId, amount, method);
                    payment.setStatus(status);
                    payment.setPaymentDate(paymentDate);
                    payments.add(payment);
                } catch (Exception e) {
                    System.err.println("Erreur lors du chargement du paiement: " + e.getMessage());
                }
            }
        }
        
        return payments;
    }

    /**
     * Sauvegarde tous les paiements dans le CSV
     */
    public void saveAll(List<Payment> payments) {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"id", "reservationId", "amount", "method", "status", "paymentDate"});
        
        for (Payment payment : payments) {
            rows.add(new String[]{
                payment.getId(),
                payment.getReservationId(),
                payment.getAmount().toString(),
                payment.getMethod().name(),
                payment.getStatus().name(),
                payment.getPaymentDate() == null ? "" : payment.getPaymentDate().format(DATE_FORMATTER)
            });
        }
        
        csvService.writeCsv(CSV_FILE, rows);
    }
}

