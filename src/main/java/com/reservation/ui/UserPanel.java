package com.reservation.ui;

import com.reservation.factory.UserType;
import com.reservation.model.User;
import com.reservation.service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panneau de gestion des utilisateurs
 */
public class UserPanel extends JPanel {
    private final UserService userService;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField idField;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JComboBox<UserType> typeCombo;

    public UserPanel(UserService userService) {
        this.userService = userService;
        initializeComponents();
        layoutComponents();
        refresh();
    }

    private void initializeComponents() {
        String[] columns = {"ID", "Nom", "Email", "Téléphone", "Rôle"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.setRowHeight(25);

        idField = new JTextField(15);
        nameField = new JTextField(15);
        emailField = new JTextField(15);
        phoneField = new JTextField(15);
        typeCombo = new JComboBox<>(UserType.values());
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Formulaire de création
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
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Téléphone:"), gbc);
        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        formPanel.add(typeCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JButton createButton = new JButton("Créer Utilisateur");
        createButton.addActionListener(new CreateUserActionListener());
        formPanel.add(createButton, gbc);

        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBorder(BorderFactory.createTitledBorder("Créer un nouvel utilisateur"));
        formContainer.add(formPanel, BorderLayout.CENTER);

        // Tableau des utilisateurs
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Liste des utilisateurs"));
        scrollPane.setPreferredSize(new Dimension(0, 300));

        // Panneau de rafraîchissement
        JPanel refreshPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshButton = new JButton("Actualiser");
        refreshButton.addActionListener(e -> refresh());
        refreshPanel.add(refreshButton);

        // Layout principal
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formContainer, BorderLayout.NORTH);
        topPanel.add(refreshPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private class CreateUserActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String id = idField.getText().trim();
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();
                UserType type = (UserType) typeCombo.getSelectedItem();

                if (id.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(UserPanel.this,
                        "Veuillez remplir tous les champs",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                User user = userService.createUser(type, id, name, email, phone);
                JOptionPane.showMessageDialog(UserPanel.this,
                    "Utilisateur créé avec succès: " + user.getName(),
                    "Succès", JOptionPane.INFORMATION_MESSAGE);

                // Réinitialiser les champs
                idField.setText("");
                nameField.setText("");
                emailField.setText("");
                phoneField.setText("");

                refresh();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(UserPanel.this,
                    "Erreur lors de la création: " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void refresh() {
        List<User> users = userService.getAllUsers();
        updateTable(users);
    }

    private void updateTable(List<User> users) {
        tableModel.setRowCount(0);
        for (User user : users) {
            tableModel.addRow(new Object[]{
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole()
            });
        }
    }
}

