package com.reservation.storage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Service générique pour le stockage CSV
 * Principe SOLID: SRP (Single Responsibility Principle) - Responsabilité unique de gestion CSV
 */
public class CsvStorageService {
    private static final String DATA_DIR = "data";
    
    static {
        // Créer le dossier data s'il n'existe pas
        try {
            Path dataPath = Paths.get(DATA_DIR);
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la création du dossier data: " + e.getMessage());
        }
    }

    /**
     * Lit toutes les lignes d'un fichier CSV
     */
    public List<String[]> readCsv(String filename) {
        List<String[]> data = new ArrayList<>();
        Path filePath = Paths.get(DATA_DIR, filename);
        
        if (!Files.exists(filePath)) {
            return data; // Retourner une liste vide si le fichier n'existe pas
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    data.add(line.split(","));
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier CSV " + filename + ": " + e.getMessage());
        }
        
        return data;
    }

    /**
     * Écrit des données dans un fichier CSV
     */
    public void writeCsv(String filename, List<String[]> data) {
        Path filePath = Paths.get(DATA_DIR, filename);
        
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (String[] row : data) {
                writer.write(String.join(",", row));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture du fichier CSV " + filename + ": " + e.getMessage());
        }
    }

    /**
     * Ajoute une ligne à un fichier CSV
     */
    public void appendCsv(String filename, String[] row) {
        Path filePath = Paths.get(DATA_DIR, filename);
        
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, 
                java.nio.file.StandardOpenOption.CREATE, 
                java.nio.file.StandardOpenOption.APPEND)) {
            writer.write(String.join(",", row));
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Erreur lors de l'ajout au fichier CSV " + filename + ": " + e.getMessage());
        }
    }

    /**
     * Vérifie si un fichier existe
     */
    public boolean fileExists(String filename) {
        return Files.exists(Paths.get(DATA_DIR, filename));
    }
}

