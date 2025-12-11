# Application de Réservation de Services

Application Java de réservation de services (coiffure, ménage, maintenance, coaching, etc.) développée en respectant les principes SOLID et utilisant plusieurs Design Patterns.

## 📋 Fonctionnalités

1. **Gestion des utilisateurs** (client, prestataire, admin)
2. **Catalogue de services et catégories**
3. **Réservation** (créneau, annulation, modification)
4. **Paiement** (carte, PayPal, wallet simulé)
5. **Notifications** (email, SMS, in-app)
6. **Recherche et filtres**
7. **Historique des réservations**
8. **Persistance des données** (stockage CSV automatique)

## 🏗️ Architecture

L'application suit une architecture en couches :

```
src/main/java/com/reservation/
├── model/              # Modèles de domaine
├── repository/         # Pattern Repository (avec persistance CSV)
├── service/            # Services métier
├── storage/            # Stockage CSV (persistance des données)
├── payment/            # Pattern Strategy (paiements)
├── factory/            # Pattern Factory (utilisateurs)
├── observer/           # Pattern Observer (notifications)
├── builder/            # Pattern Builder (réservations)
├── facade/             # Pattern Facade (interface simplifiée)
└── ui/                 # Interface graphique Swing
```

## 💾 Persistance des Données

L'application utilise le **stockage CSV** pour persister toutes les données :
- **Fichiers CSV** : Toutes les données sont sauvegardées dans le dossier `data/`
  - `users.csv` : Utilisateurs (clients, prestataires, admins)
  - `services.csv` : Services disponibles
  - `reservations.csv` : Réservations
  - `payments.csv` : Paiements
- **Chargement automatique** : Les données sont chargées au démarrage de l'application
- **Sauvegarde automatique** : Toute modification est immédiatement sauvegardée dans les fichiers CSV
- **Persistance** : Les données sont conservées entre les redémarrages de l'application

## 🎯 Principes SOLID Appliqués

### ✅ Single Responsibility Principle (SRP)
Chaque classe a une responsabilité unique et bien définie.

### ✅ Open/Closed Principle (OCP)
L'application est ouverte à l'extension mais fermée à la modification (via Strategy, Factory, Observer).

### ✅ Liskov Substitution Principle (LSP)
Les sous-classes (Client, Prestataire, Admin) peuvent remplacer User sans altérer le comportement.

### ✅ Interface Segregation Principle (ISP)
Interfaces spécifiques et minimales (Repository, PaymentStrategy, NotificationObserver).

### ✅ Dependency Inversion Principle (DIP)
Dépendance sur des abstractions (interfaces) plutôt que sur des implémentations concrètes.

## 🎨 Design Patterns Implémentés

### 1. Strategy Pattern (Comportemental)
**Utilisation**: Méthodes de paiement (Carte, PayPal, Wallet)
- Permet d'interchanger dynamiquement les stratégies de paiement
- Facilite l'ajout de nouvelles méthodes de paiement

### 2. Factory Pattern (Créationnel)
**Utilisation**: Création d'utilisateurs (Client, Prestataire, Admin)
- Centralise la logique de création
- Facilite l'ajout de nouveaux types d'utilisateurs

### 3. Observer Pattern (Comportemental)
**Utilisation**: Système de notifications (Email, SMS, In-App)
- Découplage entre le sujet et les observateurs
- Notifications automatiques à plusieurs canaux

### 4. Repository Pattern (Structurel/Comportemental)
**Utilisation**: Abstraction de l'accès aux données
- Découplage entre logique métier et persistance
- Facilite les tests et le changement de source de données

### 5. Builder Pattern (Créationnel)
**Utilisation**: Construction de réservations
- Construction flexible et lisible d'objets complexes
- Validation centralisée

### 6. Facade Pattern (Structurel)
**Utilisation**: Interface simplifiée pour les réservations
- Masque la complexité des interactions entre services
- Interface unique et simple pour le client

## 🚀 Compilation et Exécution

### Prérequis
- Java 17 ou supérieur
- Maven 3.6 ou supérieur

### Compilation
```bash
mvn clean compile
```

### Exécution

#### Interface Graphique (Recommandé)
```bash
mvn clean compile exec:java
```
L'application s'ouvrira avec une interface graphique Swing comprenant :
- **Recherche de Services** : Rechercher et filtrer les services disponibles
- **Réserver** : Créer une nouvelle réservation avec paiement
- **Mes Réservations** : Voir l'historique et annuler des réservations
- **Utilisateurs** : Gérer les utilisateurs (clients, prestataires, admins)
- **Gestion Services** : Créer, modifier et supprimer des services

#### Application Console (Alternative)
```bash
java -cp target/classes com.reservation.Application
```

## 📊 Diagrammes UML

Les diagrammes UML sont disponibles dans le dossier `docs/` :
- `diagramme-classes.puml` : Diagramme de classes (format PlantUML)
- `diagramme-sequence.puml` : Diagramme de séquence (format PlantUML)

Pour visualiser les diagrammes PlantUML :
1. Installer PlantUML : http://plantuml.com/
2. Utiliser un plugin dans votre IDE (IntelliJ, VS Code, etc.)
3. Ou utiliser un service en ligne : http://www.plantuml.com/plantuml/

## 📖 Documentation

Le rapport technique détaillé est disponible dans `docs/RAPPORT_TECHNIQUE.md`. Il contient :
- Analyse détaillée des principes SOLID
- Justification de chaque Design Pattern
- Choix architecturaux
- Points forts et améliorations possibles

## 📁 Structure du Projet

```
.
├── pom.xml                          # Configuration Maven
├── README.md                        # Ce fichier
├── docs/
│   ├── RAPPORT_TECHNIQUE.md        # Rapport technique détaillé
│   ├── diagramme-classes.puml     # Diagramme de classes
│   └── diagramme-sequence.puml     # Diagramme de séquence
└── src/
    └── main/
        └── java/
            └── com/
                └── reservation/
                    ├── Application.java
                    ├── ui/             # Interface graphique Swing
                    │   ├── MainWindow.java
                    │   ├── ServiceSearchPanel.java
                    │   ├── ReservationPanel.java
                    │   ├── HistoryPanel.java
                    │   ├── UserPanel.java
                    │   └── ServiceManagementPanel.java
                    ├── model/          # Modèles de domaine
                    ├── repository/     # Repositories (avec persistance CSV)
                    ├── service/        # Services métier
                    ├── storage/        # Stockage CSV
                    │   ├── CsvStorageService.java
                    │   ├── UserCsvStorage.java
                    │   ├── ServiceCsvStorage.java
                    │   ├── ReservationCsvStorage.java
                    │   └── PaymentCsvStorage.java
                    ├── payment/        # Strategy Pattern
                    ├── factory/        # Factory Pattern
                    ├── observer/       # Observer Pattern
                    ├── builder/        # Builder Pattern
                    └── facade/         # Facade Pattern
```

## 🧪 Exemple d'Utilisation

### Interface Graphique
Lancez l'application avec `mvn clean compile exec:java` pour accéder à l'interface graphique avec 5 onglets :

1. **Recherche de Services** : Recherchez des services par nom ou catégorie
2. **Réserver** : Créez une réservation en sélectionnant un service, une date et une méthode de paiement
3. **Mes Réservations** : Consultez votre historique et annulez des réservations
4. **Utilisateurs** : Créez et gérez les utilisateurs (clients, prestataires, admins)
5. **Gestion Services** : Créez, modifiez et supprimez des services (ID, nom, description, catégorie, prix, durée, prestataire)

### Code Java

```java
// Création d'utilisateurs (Factory Pattern)
User client = userService.createUser(
    UserType.CLIENT, "C001", "Jean Dupont", 
    "jean@example.com", "0123456789"
);

// Réservation complète (Facade Pattern)
Reservation reservation = reservationFacade.completeReservation(
    "C001", "S001", "P001",
    LocalDateTime.now().plusDays(1),
    PaymentMethod.CREDIT_CARD,
    "1234-5678-9012-3456"
);
```

## ✨ Points Forts

1. **Respect strict des principes SOLID**
2. **6 Design Patterns bien justifiés**
3. **Architecture claire et modulaire**
4. **Code extensible et maintenable**
5. **Documentation complète**

## 🔄 Améliorations Possibles

- Ajout d'une couche de validation
- Gestion d'erreurs plus robuste
- Système de logging
- Tests unitaires complets
- API REST
- Base de données réelle

## 👨‍💻 Auteur

Développé dans le cadre d'un projet académique sur les Design Patterns et les principes SOLID.

## 📝 Licence

Ce projet est à des fins éducatives.

