package com.reservation.storage;

import com.reservation.model.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Service de stockage CSV pour les services
 */
public class ServiceCsvStorage {
    private static final String CSV_FILE = "services.csv";
    private final CsvStorageService csvService;

    public ServiceCsvStorage() {
        this.csvService = new CsvStorageService();
        initializeCsvFile();
    }

    private void initializeCsvFile() {
        if (!csvService.fileExists(CSV_FILE)) {
            List<String[]> header = new ArrayList<>();
            header.add(new String[]{"id", "name", "description", "category", "price", "duration", "prestataireId"});
            csvService.writeCsv(CSV_FILE, header);
        }
    }

    /**
     * Charge tous les services depuis le CSV
     */
    public List<Service> loadAll() {
        List<Service> services = new ArrayList<>();
        List<String[]> rows = csvService.readCsv(CSV_FILE);
        
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row.length >= 7) {
                try {
                    String id = row[0];
                    String name = row[1];
                    String description = row[2];
                    String category = row[3];
                    BigDecimal price = new BigDecimal(row[4]);
                    int duration = Integer.parseInt(row[5]);
                    String prestataireId = row[6];
                    
                    services.add(new Service(id, name, description, category, price, duration, prestataireId));
                } catch (Exception e) {
                    System.err.println("Erreur lors du chargement du service: " + e.getMessage());
                }
            }
        }
        
        return services;
    }

    /**
     * Sauvegarde tous les services dans le CSV
     */
    public void saveAll(List<Service> services) {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"id", "name", "description", "category", "price", "duration", "prestataireId"});
        
        for (Service service : services) {
            rows.add(new String[]{
                service.getId(),
                service.getName(),
                service.getDescription(),
                service.getCategory(),
                service.getPrice().toString(),
                String.valueOf(service.getDuration()),
                service.getPrestataireId()
            });
        }
        
        csvService.writeCsv(CSV_FILE, rows);
    }
}

