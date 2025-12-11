package com.reservation.ui;

import com.reservation.model.Service;
import com.reservation.service.ServiceService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;

/**
 * Panneau de gestion des services
 */
public class ServiceManagementPanel extends JPanel {
    private final ServiceService serviceService;
    private JTable serviceTable;
    private DefaultTableModel tableModel;
    private JTextField idField;
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JComboBox<String> categoryCombo;
    private JTextField priceField;
    private JTextField durationField;
    private JTextField prestataireIdField;
    private JButton createButton;
    private JButton updateButton;
    private JButton deleteButton;

    public ServiceManagementPanel(ServiceService serviceService) {
        this.serviceService = serviceService;
        initializeComponents();
        layoutComponents();
        refresh();
    }

    private void initializeComponents() {
        String[] columns = {"ID", "Nom", "Description", "Catégorie", "Prix", "Durée (min)", "Prestataire"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        serviceTable = new JTable(tableModel);
        serviceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        serviceTable.setRowHeight(25);
        serviceTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedService();
            }
        });

        // Ajuster la largeur des colonnes
        serviceTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        serviceTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        serviceTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        serviceTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        serviceTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        serviceTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        serviceTable.getColumnModel().getColumn(6).setPreferredWidth(100);

        idField = new JTextField(15);
        nameField = new JTextField(15);
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        categoryCombo = new JComboBox<>(new String[]{"Coiffure", "Ménage", "Maintenance", "Coaching", "Autre"});
        priceField = new JTextField(15);
        durationField = new JTextField(15);
        prestataireIdField = new JTextField(15);

        createButton = new JButton("Créer");
        updateButton = new JButton("Modifier");
        deleteButton = new JButton("Supprimer");
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Formulaire de création/modification
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Nom:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(new JScrollPane(descriptionArea), gbc);
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Catégorie:"), gbc);
        gbc.gridx = 1;
        formPanel.add(categoryCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Prix (€):"), gbc);
        gbc.gridx = 1;
        formPanel.add(priceField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Durée (minutes):"), gbc);
        gbc.gridx = 1;
        formPanel.add(durationField, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Prestataire ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(prestataireIdField, gbc);

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(createButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        
        JButton clearButton = new JButton("Nouveau");
        clearButton.addActionListener(e -> clearForm());
        buttonPanel.add(clearButton);
        
        JButton refreshButton = new JButton("Actualiser");
        refreshButton.addActionListener(e -> refresh());
        buttonPanel.add(refreshButton);

        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(buttonPanel, gbc);

        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBorder(BorderFactory.createTitledBorder("Créer/Modifier un service"));
        formContainer.add(formPanel, BorderLayout.CENTER);

        // Tableau des services
        JScrollPane scrollPane = new JScrollPane(serviceTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Liste des services"));
        scrollPane.setPreferredSize(new Dimension(0, 300));

        // Layout principal
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formContainer, BorderLayout.NORTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Actions des boutons
        createButton.addActionListener(new CreateServiceActionListener());
        updateButton.addActionListener(new UpdateServiceActionListener());
        deleteButton.addActionListener(new DeleteServiceActionListener());
    }

    private class CreateServiceActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String id = idField.getText().trim();
                String name = nameField.getText().trim();
                String description = descriptionArea.getText().trim();
                String category = (String) categoryCombo.getSelectedItem();
                String priceStr = priceField.getText().trim();
                String durationStr = durationField.getText().trim();
                String prestataireId = prestataireIdField.getText().trim();

                if (id.isEmpty() || name.isEmpty() || description.isEmpty() || 
                    priceStr.isEmpty() || durationStr.isEmpty() || prestataireId.isEmpty()) {
                    JOptionPane.showMessageDialog(ServiceManagementPanel.this,
                        "Veuillez remplir tous les champs",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                BigDecimal price = new BigDecimal(priceStr);
                int duration = Integer.parseInt(durationStr);

                if (price.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(ServiceManagementPanel.this,
                        "Le prix doit être supérieur à 0",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (duration <= 0) {
                    JOptionPane.showMessageDialog(ServiceManagementPanel.this,
                        "La durée doit être supérieure à 0",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Service service = serviceService.createService(
                    id, name, description, category, price, duration, prestataireId
                );

                JOptionPane.showMessageDialog(ServiceManagementPanel.this,
                    "Service créé avec succès: " + service.getName(),
                    "Succès", JOptionPane.INFORMATION_MESSAGE);

                clearForm();
                refresh();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(ServiceManagementPanel.this,
                    "Erreur de format: Le prix et la durée doivent être des nombres valides",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(ServiceManagementPanel.this,
                    "Erreur lors de la création: " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class UpdateServiceActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String id = idField.getText().trim();
                String name = nameField.getText().trim();
                String description = descriptionArea.getText().trim();
                String category = (String) categoryCombo.getSelectedItem();
                String priceStr = priceField.getText().trim();
                String durationStr = durationField.getText().trim();
                String prestataireId = prestataireIdField.getText().trim();

                if (id.isEmpty() || name.isEmpty() || description.isEmpty() || 
                    priceStr.isEmpty() || durationStr.isEmpty() || prestataireId.isEmpty()) {
                    JOptionPane.showMessageDialog(ServiceManagementPanel.this,
                        "Veuillez remplir tous les champs",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                BigDecimal price = new BigDecimal(priceStr);
                int duration = Integer.parseInt(durationStr);

                Service service = serviceService.updateService(
                    id, name, description, category, price, duration, prestataireId
                );

                JOptionPane.showMessageDialog(ServiceManagementPanel.this,
                    "Service modifié avec succès: " + service.getName(),
                    "Succès", JOptionPane.INFORMATION_MESSAGE);

                clearForm();
                refresh();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(ServiceManagementPanel.this,
                    "Erreur de format: Le prix et la durée doivent être des nombres valides",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(ServiceManagementPanel.this,
                    "Erreur lors de la modification: " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class DeleteServiceActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String id = idField.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(ServiceManagementPanel.this,
                    "Veuillez sélectionner un service à supprimer",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(ServiceManagementPanel.this,
                "Êtes-vous sûr de vouloir supprimer le service " + id + "?",
                "Confirmer la suppression", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    serviceService.deleteService(id);
                    JOptionPane.showMessageDialog(ServiceManagementPanel.this,
                        "Service supprimé avec succès",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    refresh();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ServiceManagementPanel.this,
                        "Erreur lors de la suppression: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void loadSelectedService() {
        int selectedRow = serviceTable.getSelectedRow();
        if (selectedRow >= 0) {
            String serviceId = (String) tableModel.getValueAt(selectedRow, 0);
            serviceService.getServiceById(serviceId).ifPresent(service -> {
                idField.setText(service.getId());
                nameField.setText(service.getName());
                descriptionArea.setText(service.getDescription());
                categoryCombo.setSelectedItem(service.getCategory());
                priceField.setText(service.getPrice().toString());
                durationField.setText(String.valueOf(service.getDuration()));
                prestataireIdField.setText(service.getPrestataireId());
                
                idField.setEditable(false);
                createButton.setEnabled(false);
                updateButton.setEnabled(true);
                deleteButton.setEnabled(true);
            });
        }
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        descriptionArea.setText("");
        categoryCombo.setSelectedIndex(0);
        priceField.setText("");
        durationField.setText("");
        prestataireIdField.setText("");
        
        idField.setEditable(true);
        serviceTable.clearSelection();
        createButton.setEnabled(true);
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    public void refresh() {
        List<Service> services = serviceService.getAllServices();
        updateTable(services);
    }

    private void updateTable(List<Service> services) {
        tableModel.setRowCount(0);
        for (Service service : services) {
            tableModel.addRow(new Object[]{
                service.getId(),
                service.getName(),
                service.getDescription(),
                service.getCategory(),
                service.getPrice() + "€",
                service.getDuration(),
                service.getPrestataireId()
            });
        }
    }
}

