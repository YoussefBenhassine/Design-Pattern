package com.reservation.ui;

import com.reservation.model.Service;
import com.reservation.service.ServiceSearchService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panneau de recherche de services
 */
public class ServiceSearchPanel extends JPanel {
    private final ServiceSearchService searchService;
    private JTextField searchField;
    private JComboBox<String> categoryCombo;
    private JTable serviceTable;
    private DefaultTableModel tableModel;

    public ServiceSearchPanel(ServiceSearchService searchService) {
        this.searchService = searchService;
        initializeComponents();
        layoutComponents();
        refresh();
    }

    private void initializeComponents() {
        searchField = new JTextField(20);
        categoryCombo = new JComboBox<>(new String[]{"Toutes", "Coiffure", "Ménage", "Maintenance", "Coaching"});
        
        // Tableau des services
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
        
        // Ajuster la largeur des colonnes
        serviceTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        serviceTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        serviceTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        serviceTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        serviceTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        serviceTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        serviceTable.getColumnModel().getColumn(6).setPreferredWidth(100);
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panneau de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Rechercher:"));
        searchPanel.add(searchField);
        searchPanel.add(new JLabel("Catégorie:"));
        searchPanel.add(categoryCombo);
        
        JButton searchButton = new JButton("Rechercher");
        searchButton.addActionListener(new SearchActionListener());
        searchPanel.add(searchButton);
        
        JButton refreshButton = new JButton("Actualiser");
        refreshButton.addActionListener(e -> refresh());
        searchPanel.add(refreshButton);

        add(searchPanel, BorderLayout.NORTH);

        // Tableau avec scroll
        JScrollPane scrollPane = new JScrollPane(serviceTable);
        scrollPane.setPreferredSize(new Dimension(0, 400));
        add(scrollPane, BorderLayout.CENTER);

        // Panneau d'information
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.add(new JLabel("Sélectionnez un service pour voir les détails"));
        add(infoPanel, BorderLayout.SOUTH);
    }

    private class SearchActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            performSearch();
        }
    }

    private void performSearch() {
        String searchText = searchField.getText().trim();
        String category = (String) categoryCombo.getSelectedItem();
        
        List<Service> services;
        
        if ("Toutes".equals(category) && searchText.isEmpty()) {
            services = searchService.searchAll();
        } else if ("Toutes".equals(category)) {
            services = searchService.searchByName(searchText);
        } else if (searchText.isEmpty()) {
            services = searchService.searchByCategory(category);
        } else {
            services = searchService.search(category, null, searchText);
        }
        
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

    public void refresh() {
        List<Service> services = searchService.searchAll();
        updateTable(services);
    }

    public Service getSelectedService() {
        int selectedRow = serviceTable.getSelectedRow();
        if (selectedRow >= 0) {
            String serviceId = (String) tableModel.getValueAt(selectedRow, 0);
            return searchService.searchAll().stream()
                .filter(s -> s.getId().equals(serviceId))
                .findFirst()
                .orElse(null);
        }
        return null;
    }
}

