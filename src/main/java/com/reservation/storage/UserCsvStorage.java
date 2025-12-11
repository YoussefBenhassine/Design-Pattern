package com.reservation.storage;

import com.reservation.factory.UserFactory;
import com.reservation.factory.UserType;
import com.reservation.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Service de stockage CSV pour les utilisateurs
 */
public class UserCsvStorage {
    private static final String CSV_FILE = "users.csv";
    private final CsvStorageService csvService;

    public UserCsvStorage() {
        this.csvService = new CsvStorageService();
        initializeCsvFile();
    }

    private void initializeCsvFile() {
        if (!csvService.fileExists(CSV_FILE)) {
            // Créer le fichier avec l'en-tête
            List<String[]> header = new ArrayList<>();
            header.add(new String[]{"id", "name", "email", "phone", "type"});
            csvService.writeCsv(CSV_FILE, header);
        }
    }

    /**
     * Charge tous les utilisateurs depuis le CSV
     */
    public List<User> loadAll() {
        List<User> users = new ArrayList<>();
        List<String[]> rows = csvService.readCsv(CSV_FILE);
        
        // Ignorer la première ligne (en-tête)
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row.length >= 5) {
                try {
                    String id = row[0];
                    String name = row[1];
                    String email = row[2];
                    String phone = row[3];
                    UserType userType = UserType.valueOf(row[4]);
                    
                    User user = UserFactory.createUser(userType, id, name, email, phone);
                    users.add(user);
                } catch (Exception e) {
                    System.err.println("Erreur lors du chargement de l'utilisateur: " + e.getMessage());
                }
            }
        }
        
        return users;
    }

    /**
     * Sauvegarde tous les utilisateurs dans le CSV
     */
    public void saveAll(List<User> users) {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"id", "name", "email", "phone", "type"});
        
        for (User user : users) {
            rows.add(new String[]{
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                getUserType(user)
            });
        }
        
        csvService.writeCsv(CSV_FILE, rows);
    }

    /**
     * Ajoute un utilisateur au CSV
     */
    public void append(User user) {
        csvService.appendCsv(CSV_FILE, new String[]{
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            getUserType(user)
        });
    }

    /**
     * Détermine le type d'utilisateur à partir de son rôle
     */
    private String getUserType(User user) {
        String role = user.getRole();
        if (role.equals("CLIENT")) {
            return "CLIENT";
        } else if (role.equals("PRESTATAIRE")) {
            return "PRESTATAIRE";
        } else if (role.equals("ADMIN")) {
            return "ADMIN";
        }
        return "CLIENT"; // Par défaut
    }
}

