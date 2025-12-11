package com.reservation.ui;

import com.reservation.facade.ReservationFacade;
import com.reservation.factory.UserType;
import com.reservation.model.*;
import com.reservation.repository.*;
import com.reservation.service.*;

import javax.swing.*;
import java.math.BigDecimal;

/**
 * Fenêtre principale de l'application de réservation
 * Interface graphique Swing
 */
public class MainWindow extends JFrame {
    private final ReservationFacade reservationFacade;
    private final UserService userService;
    private final ServiceSearchService searchService;
    private final ServiceService serviceService;
    private final ReservationService reservationService;
    private final NotificationService notificationService;
    
    private JTabbedPane tabbedPane;
    private ServiceSearchPanel searchPanel;
    private ReservationPanel reservationPanel;
    private HistoryPanel historyPanel;
    private UserPanel userPanel;
    private ServiceManagementPanel serviceManagementPanel;
    
    private User currentUser;

    public MainWindow(ReservationFacade reservationFacade,
                     UserService userService,
                     ServiceSearchService searchService,
                     ServiceService serviceService,
                     ReservationService reservationService,
                     NotificationService notificationService) {
        this.reservationFacade = reservationFacade;
        this.userService = userService;
        this.searchService = searchService;
        this.serviceService = serviceService;
        this.reservationService = reservationService;
        this.notificationService = notificationService;
        
        initializeGUI();
        setupInitialUser();
    }

    private void initializeGUI() {
        setTitle("Application de Réservation de Services");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        // Créer le menu
        createMenuBar();
        
        // Créer les onglets
        tabbedPane = new JTabbedPane();
        
        // Panneau de recherche
        searchPanel = new ServiceSearchPanel(searchService);
        tabbedPane.addTab("Recherche de Services", searchPanel);
        
        // Panneau de réservation
        reservationPanel = new ReservationPanel(reservationFacade, searchService);
        tabbedPane.addTab("Réserver", reservationPanel);
        
        // Panneau d'historique
        historyPanel = new HistoryPanel(reservationService);
        tabbedPane.addTab("Mes Réservations", historyPanel);
        
        // Panneau utilisateur
        userPanel = new UserPanel(userService);
        tabbedPane.addTab("Utilisateurs", userPanel);
        
        // Panneau de gestion des services
        serviceManagementPanel = new ServiceManagementPanel(serviceService);
        tabbedPane.addTab("Gestion Services", serviceManagementPanel);
        
        add(tabbedPane);
        
        // Rafraîchir les panneaux
        refreshAllPanels();
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu("Fichier");
        JMenuItem exitItem = new JMenuItem("Quitter");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        
        JMenu helpMenu = new JMenu("Aide");
        JMenuItem aboutItem = new JMenuItem("À propos");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }

    private void setupInitialUser() {
        // Créer un utilisateur par défaut pour la démo
        try {
            currentUser = userService.findUserById("C001").orElse(null);
            if (currentUser == null) {
                currentUser = userService.createUser(
                    UserType.CLIENT, "C001", "Jean Dupont", 
                    "jean@example.com", "0123456789"
                );
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors de l'initialisation: " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshAllPanels() {
        if (searchPanel != null) {
            searchPanel.refresh();
        }
        if (reservationPanel != null) {
            reservationPanel.refresh();
        }
        if (historyPanel != null && currentUser != null) {
            historyPanel.refresh(currentUser.getId());
        }
        if (serviceManagementPanel != null) {
            serviceManagementPanel.refresh();
        }
    }

    private void showAboutDialog() {
        String message = "Application de Réservation de Services\n\n" +
                        "Développée avec:\n" +
                        "- Principes SOLID\n" +
                        "- Design Patterns (Strategy, Factory, Observer, Repository, Builder, Facade)\n" +
                        "- Java Swing\n\n" +
                        "Version 1.0.0";
        JOptionPane.showMessageDialog(this, message, "À propos", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        // Initialisation des repositories
        UserRepository userRepository = new UserRepository();
        ServiceRepository serviceRepository = new ServiceRepository();
        ReservationRepository reservationRepository = new ReservationRepository();
        PaymentRepository paymentRepository = new PaymentRepository();

        // Initialisation des services
        UserService userService = new UserService(userRepository);
        NotificationService notificationService = new NotificationService();
        ServiceService serviceService = new ServiceService(serviceRepository);
        ReservationService reservationService = new ReservationService(
            reservationRepository, serviceRepository, notificationService
        );
        PaymentService paymentService = new PaymentService(paymentRepository);
        ServiceSearchService searchService = new ServiceSearchService(serviceRepository);

        // Configuration des observateurs
        notificationService.attach(new com.reservation.observer.EmailNotificationObserver());
        notificationService.attach(new com.reservation.observer.SMSNotificationObserver());
        notificationService.attach(new com.reservation.observer.InAppNotificationObserver());

        // Initialisation de la Facade
        ReservationFacade reservationFacade = new ReservationFacade(
            reservationService, paymentService, notificationService
        );

        // Créer quelques données de démo
        createDemoData(userService, serviceRepository);

        // Créer et afficher la fenêtre
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            MainWindow window = new MainWindow(
                reservationFacade, userService, searchService, serviceService,
                reservationService, notificationService
            );
            window.setVisible(true);
        });
    }

    private static void createDemoData(UserService userService, ServiceRepository serviceRepository) {
        // Créer des utilisateurs de démo
        try {
            userService.createUser(UserType.CLIENT, "C001", "Jean Dupont", 
                "jean@example.com", "0123456789");
            userService.createUser(UserType.PRESTATAIRE, "P001", "Marie Martin", 
                "marie@example.com", "0987654321");
            userService.createUser(UserType.PRESTATAIRE, "P002", "Pierre Dubois", 
                "pierre@example.com", "0111222333");
        } catch (Exception e) {
            // Utilisateurs déjà créés
        }

        // Créer des services de démo
        Service service1 = new Service("S001", "Coupe de cheveux", 
            "Coupe moderne et tendance", "Coiffure", 
            new BigDecimal("25.00"), 30, "P001");
        Service service2 = new Service("S002", "Ménage complet", 
            "Nettoyage complet de la maison", "Ménage", 
            new BigDecimal("50.00"), 120, "P001");
        Service service3 = new Service("S003", "Réparation plomberie", 
            "Réparation de fuites et installations", "Maintenance", 
            new BigDecimal("80.00"), 60, "P002");
        Service service4 = new Service("S004", "Coaching sportif", 
            "Séance de coaching personnalisée", "Coaching", 
            new BigDecimal("40.00"), 60, "P002");

        serviceRepository.save(service1);
        serviceRepository.save(service2);
        serviceRepository.save(service3);
        serviceRepository.save(service4);
    }
}

