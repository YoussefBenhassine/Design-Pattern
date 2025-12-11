package com.reservation.ui;

import com.reservation.model.Reservation;
import com.reservation.service.ReservationService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Panneau d'historique des réservations
 */
public class HistoryPanel extends JPanel {
    private final ReservationService reservationService;
    private JTable reservationTable;
    private DefaultTableModel tableModel;
    private JTextField clientIdField;
    private JButton refreshButton;
    private JButton cancelButton;

    public HistoryPanel(ReservationService reservationService) {
        this.reservationService = reservationService;
        initializeComponents();
        layoutComponents();
    }

    private void initializeComponents() {
        String[] columns = {"ID", "Service", "Prestataire", "Date/Heure", "Montant", "Statut"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reservationTable = new JTable(tableModel);
        reservationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reservationTable.setRowHeight(25);

        clientIdField = new JTextField(20);
        clientIdField.setText("C001");
        refreshButton = new JButton("Actualiser");
        cancelButton = new JButton("Annuler la réservation sélectionnée");
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panneau de contrôle
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.add(new JLabel("Client ID:"));
        controlPanel.add(clientIdField);
        controlPanel.add(refreshButton);
        controlPanel.add(cancelButton);

        refreshButton.addActionListener(e -> {
            String clientId = clientIdField.getText().trim();
            if (!clientId.isEmpty()) {
                refresh(clientId);
            }
        });

        cancelButton.addActionListener(new CancelActionListener());

        add(controlPanel, BorderLayout.NORTH);

        // Tableau des réservations
        JScrollPane scrollPane = new JScrollPane(reservationTable);
        scrollPane.setPreferredSize(new Dimension(0, 400));
        add(scrollPane, BorderLayout.CENTER);

        // Panneau d'information
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.add(new JLabel("Sélectionnez une réservation pour voir les détails ou l'annuler"));
        add(infoPanel, BorderLayout.SOUTH);
    }

    private class CancelActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = reservationTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(HistoryPanel.this,
                    "Veuillez sélectionner une réservation à annuler",
                    "Aucune sélection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String reservationId = (String) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(HistoryPanel.this,
                "Êtes-vous sûr de vouloir annuler la réservation " + reservationId + "?",
                "Confirmer l'annulation", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    reservationService.cancelReservation(reservationId);
                    JOptionPane.showMessageDialog(HistoryPanel.this,
                        "Réservation annulée avec succès",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
                    refresh(clientIdField.getText().trim());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(HistoryPanel.this,
                        "Erreur lors de l'annulation: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public void refresh(String clientId) {
        List<Reservation> reservations = reservationService.getClientHistory(clientId);
        updateTable(reservations);
    }

    private void updateTable(List<Reservation> reservations) {
        tableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        for (Reservation reservation : reservations) {
            tableModel.addRow(new Object[]{
                reservation.getId(),
                reservation.getServiceId(),
                reservation.getPrestataireId(),
                reservation.getDateTime().format(formatter),
                reservation.getTotalAmount() + "€",
                reservation.getStatus()
            });
        }
    }
}

