package com.reservation;

import com.reservation.facade.ReservationFacade;
import com.reservation.factory.UserType;
import com.reservation.model.*;
import com.reservation.observer.*;
import com.reservation.repository.*;
import com.reservation.service.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Classe principale de démonstration de l'application
 */
public class Application {
    public static void main(String[] args) {
        System.out.println("=== Application de Réservation de Services ===\n");

        // Initialisation des repositories
        UserRepository userRepository = new UserRepository();
        ServiceRepository serviceRepository = new ServiceRepository();
        ReservationRepository reservationRepository = new ReservationRepository();
        PaymentRepository paymentRepository = new PaymentRepository();

        // Initialisation des services
        UserService userService = new UserService(userRepository);
        NotificationService notificationService = new NotificationService();
        ReservationService reservationService = new ReservationService(
            reservationRepository, serviceRepository, notificationService
        );
        PaymentService paymentService = new PaymentService(paymentRepository);
        ServiceSearchService searchService = new ServiceSearchService(serviceRepository);

        // Configuration des observateurs de notification
        notificationService.attach(new EmailNotificationObserver());
        notificationService.attach(new SMSNotificationObserver());
        notificationService.attach(new InAppNotificationObserver());

        // Initialisation de la Facade
        ReservationFacade reservationFacade = new ReservationFacade(
            reservationService, paymentService, notificationService
        );

        // Création d'utilisateurs (Factory Pattern)
        System.out.println("1. Création d'utilisateurs (Factory Pattern):");
        User client = userService.createUser(
            UserType.CLIENT, "C001", "Jean Dupont", "jean@example.com", "0123456789"
        );
        System.out.println("   Client créé: " + client.getName() + " (" + client.getRole() + ")");

        User prestataire = userService.createUser(
            UserType.PRESTATAIRE, "P001", "Marie Martin", "marie@example.com", "0987654321"
        );
        System.out.println("   Prestataire créé: " + prestataire.getName() + " (" + prestataire.getRole() + ")\n");

        // Création de services
        System.out.println("2. Création de services:");
        Service service1 = new Service(
            "S001", "Coupe de cheveux", "Coupe moderne", "Coiffure",
            new BigDecimal("25.00"), 30, "P001"
        );
        serviceRepository.save(service1);
        System.out.println("   Service créé: " + service1.getName() + " - " + service1.getPrice() + "€");

        Service service2 = new Service(
            "S002", "Ménage complet", "Nettoyage complet de la maison", "Ménage",
            new BigDecimal("50.00"), 120, "P001"
        );
        serviceRepository.save(service2);
        System.out.println("   Service créé: " + service2.getName() + " - " + service2.getPrice() + "€\n");

        // Recherche de services
        System.out.println("3. Recherche de services:");
        var services = searchService.searchByCategory("Coiffure");
        System.out.println("   Services de coiffure trouvés: " + services.size());
        services.forEach(s -> System.out.println("   - " + s.getName() + " (" + s.getPrice() + "€)"));

        // Réservation complète avec Facade Pattern
        System.out.println("\n4. Réservation complète (Facade Pattern):");
        try {
            Reservation reservation = reservationFacade.completeReservation(
                "C001", "S001", "P001",
                LocalDateTime.now().plusDays(1),
                PaymentMethod.CREDIT_CARD,
                "1234-5678-9012-3456"
            );
            System.out.println("   Réservation créée: " + reservation.getId());
            System.out.println("   Statut: " + reservation.getStatus());
            System.out.println("   Montant: " + reservation.getTotalAmount() + "€\n");
        } catch (Exception e) {
            System.out.println("   Erreur: " + e.getMessage());
        }

        // Historique des réservations
        System.out.println("5. Historique des réservations:");
        var history = reservationService.getClientHistory("C001");
        System.out.println("   Nombre de réservations: " + history.size());
        history.forEach(r -> System.out.println("   - Réservation " + r.getId() + 
            " (" + r.getStatus() + ") - " + r.getTotalAmount() + "€"));

        // Annulation avec remboursement
        System.out.println("\n6. Annulation avec remboursement:");
        if (!history.isEmpty()) {
            reservationFacade.cancelReservationWithRefund(history.get(0).getId());
            System.out.println("   Réservation annulée et remboursement effectué");
        }

        System.out.println("\n=== Fin de la démonstration ===");
    }
}

