package com.reservation.ui;

import com.reservation.facade.ReservationFacade;
import com.reservation.model.*;
import com.reservation.service.ServiceSearchService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Panneau de réservation
 */
public class ReservationPanel extends JPanel {
    private final ReservationFacade reservationFacade;
    private final ServiceSearchService searchService;
    
    private JComboBox<String> serviceCombo;
    private JTextField prestataireField;
    private JTextField clientIdField;
    private JTextField dateField;
    private JTextField timeField;
    private JComboBox<PaymentMethod> paymentMethodCombo;
    private JTextField paymentDetailsField;
    private JTextArea resultArea;

    public ReservationPanel(ReservationFacade reservationFacade, 
                           ServiceSearchService searchService) {
        this.reservationFacade = reservationFacade;
        this.searchService = searchService;
        initializeComponents();
        layoutComponents();
        setupServiceSelectionListener();
        refresh();
    }

    private void initializeComponents() {
        serviceCombo = new JComboBox<>();
        prestataireField = new JTextField(20);
        prestataireField.setEditable(false);
        clientIdField = new JTextField(20);
        clientIdField.setText("C001");
        dateField = new JTextField(20);
        timeField = new JTextField(20);
        paymentMethodCombo = new JComboBox<>(PaymentMethod.values());
        paymentDetailsField = new JTextField(20);
        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        resultArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Formulaire de réservation
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Service
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Service:"), gbc);
        gbc.gridx = 1;
        formPanel.add(serviceCombo, gbc);

        // Prestataire
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Prestataire ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(prestataireField, gbc);

        // Client
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Client ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(clientIdField, gbc);

        // Date
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        formPanel.add(dateField, gbc);

        // Heure
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Heure (HH:MM):"), gbc);
        gbc.gridx = 1;
        formPanel.add(timeField, gbc);

        // Méthode de paiement
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Méthode de paiement:"), gbc);
        gbc.gridx = 1;
        formPanel.add(paymentMethodCombo, gbc);

        // Détails paiement
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Détails paiement:"), gbc);
        gbc.gridx = 1;
        formPanel.add(paymentDetailsField, gbc);

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton reserveButton = new JButton("Réserver");
        reserveButton.addActionListener(new ReserveActionListener());
        buttonPanel.add(reserveButton);
        
        JButton refreshButton = new JButton("Actualiser");
        refreshButton.addActionListener(e -> refresh());
        buttonPanel.add(refreshButton);
        
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.NORTH);

        // Zone de résultat
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Résultat"));
        add(scrollPane, BorderLayout.CENTER);
    }

    private class ReserveActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String serviceId = (String) serviceCombo.getSelectedItem();
                if (serviceId == null || serviceId.isEmpty()) {
                    resultArea.setText("Erreur: Veuillez sélectionner un service");
                    return;
                }

                String prestataireId = prestataireField.getText().trim();
                String clientId = clientIdField.getText().trim();
                String dateStr = dateField.getText().trim();
                String timeStr = timeField.getText().trim();
                PaymentMethod paymentMethod = (PaymentMethod) paymentMethodCombo.getSelectedItem();
                String paymentDetails = paymentDetailsField.getText().trim();

                if (prestataireId.isEmpty() || clientId.isEmpty() || 
                    dateStr.isEmpty() || timeStr.isEmpty() || paymentDetails.isEmpty()) {
                    resultArea.setText("Erreur: Veuillez remplir tous les champs");
                    return;
                }

                // Parser la date et l'heure
                LocalDateTime dateTime = LocalDateTime.parse(
                    dateStr + "T" + timeStr,
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME
                );

                // Effectuer la réservation
                Reservation reservation = reservationFacade.completeReservation(
                    clientId, serviceId, prestataireId, dateTime,
                    paymentMethod, paymentDetails
                );

                resultArea.setText("✓ Réservation créée avec succès!\n\n" +
                    "ID Réservation: " + reservation.getId() + "\n" +
                    "Service: " + serviceId + "\n" +
                    "Date: " + dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n" +
                    "Montant: " + reservation.getTotalAmount() + "€\n" +
                    "Statut: " + reservation.getStatus() + "\n\n" +
                    "Le paiement a été traité et les notifications ont été envoyées.");

            } catch (Exception ex) {
                resultArea.setText("Erreur lors de la réservation:\n" + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    public void refresh() {
        // Mettre à jour la liste des services
        String selectedService = (String) serviceCombo.getSelectedItem();
        serviceCombo.removeAllItems();
        searchService.searchAll().forEach(service -> {
            serviceCombo.addItem(service.getId());
        });
        
        // Restaurer la sélection si elle existe toujours
        if (selectedService != null) {
            for (int i = 0; i < serviceCombo.getItemCount(); i++) {
                if (serviceCombo.getItemAt(i).equals(selectedService)) {
                    serviceCombo.setSelectedIndex(i);
                    break;
                }
            }
        }
    }
    
    private void setupServiceSelectionListener() {
        // Mettre à jour le prestataire quand un service est sélectionné
        serviceCombo.addActionListener(e -> {
            String serviceId = (String) serviceCombo.getSelectedItem();
            if (serviceId != null) {
                searchService.searchAll().stream()
                    .filter(s -> s.getId().equals(serviceId))
                    .findFirst()
                    .ifPresent(service -> {
                        prestataireField.setText(service.getPrestataireId());
                    });
            }
        });
    }
}

