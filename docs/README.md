# Documentation - Application de Réservation de Services

Ce dossier contient toute la documentation technique de l'application.

## 📄 Fichiers Disponibles

### 1. RAPPORT_TECHNIQUE.md
Rapport technique complet détaillant :
- L'architecture en couches
- L'application des principes SOLID (avec scores détaillés)
- Les 6 Design Patterns implémentés avec justifications
- Les choix architecturaux
- L'interface graphique
- Les diagrammes UML
- Les points forts et améliorations possibles

### 2. diagramme-classes.puml
Diagramme de classes UML au format PlantUML montrant :
- Les modèles de domaine (User, Service, Reservation, Payment, Notification)
- Les Design Patterns (Strategy, Factory, Observer, Repository, Builder, Facade)
- Les services métier
- Les composants de l'interface graphique (UI)
- Les relations entre toutes les classes

**Pour visualiser** :
- Utiliser un plugin PlantUML dans votre IDE (IntelliJ, VS Code, etc.)
- Ou utiliser un service en ligne : http://www.plantuml.com/plantuml/

### 3. diagramme-sequence.puml
Diagramme de séquence UML montrant le flux complet d'une réservation avec paiement via l'interface graphique :
- Interaction utilisateur → Interface → Services
- Utilisation du Facade Pattern
- Processus de notification (Observer Pattern)
- Gestion des paiements (Strategy Pattern)

### 4. diagramme-sequence-ui.puml
Diagramme de séquence détaillé montrant toutes les interactions possibles avec l'interface graphique :
- Initialisation de l'application
- Recherche de services
- Création de réservations
- Consultation de l'historique
- Annulation de réservations
- Gestion des utilisateurs

## 🎯 Utilisation des Diagrammes

### Visualisation avec PlantUML

1. **En ligne** : Copiez le contenu d'un fichier `.puml` et collez-le sur http://www.plantuml.com/plantuml/

2. **Dans IntelliJ IDEA** :
   - Installez le plugin "PlantUML integration"
   - Ouvrez un fichier `.puml`
   - Utilisez `Alt+D` pour prévisualiser

3. **Dans VS Code** :
   - Installez l'extension "PlantUML"
   - Ouvrez un fichier `.puml`
   - Utilisez `Ctrl+Shift+P` → "PlantUML: Preview Current Diagram"

4. **En ligne de commande** :
   ```bash
   # Installer PlantUML
   # Puis générer les images
   plantuml diagramme-classes.puml
   plantuml diagramme-sequence.puml
   plantuml diagramme-sequence-ui.puml
   ```

## 📊 Structure des Diagrammes

### Diagramme de Classes
- **Modèles** : Entités métier (User, Service, Reservation, etc.)
- **Patterns** : Implémentations des Design Patterns
- **Services** : Couche métier
- **UI** : Composants de l'interface graphique
- **Relations** : Associations, dépendances, héritage

### Diagrammes de Séquence
- **Acteurs** : Utilisateur
- **Participants** : Composants de l'application
- **Messages** : Interactions entre composants
- **Activations** : Périodes d'activité des composants

## 🔄 Mise à Jour

Les diagrammes sont maintenus à jour avec le code source. Lors de modifications importantes :
1. Mettre à jour les diagrammes PlantUML correspondants
2. Régénérer les images si nécessaire
3. Mettre à jour le rapport technique

## 📝 Notes

- Tous les diagrammes utilisent la notation UML standard
- Les diagrammes PlantUML sont en français pour correspondre au code
- Les relations montrent les dépendances réelles du code
- Les Design Patterns sont clairement identifiés dans les diagrammes

