# Projet-Multimedia

## Aperçu de l'accueil de l'application
![tableau de bord de l'application](application/src/main/resources/accueil.png)


## Description
Projet-Multimedia est un projet Universitaire développé lors d'une SAE de 3ème année à l'IUT du Havre. Le but de ce projet est de manipuler des images au format PNG en utilisant le langage de programmation Java. L'application ressemble à des logiciels de retouche d'images basiques comme Paint.

## Fonctionnalités

### Gestion des images
- Chargement et affichage d'images au format PNG
- Sauvegarde de l'image modifiée au format PNG
- Gestion de pile de couches (layers)
- Superposition et remplacement d'images
- Placement interactif des images

### Texte avec image de fond
- Création de texte avec une image en arrière-plan
- Sélection personnalisée de la police et de la taille
- Sélection d'une région spécifique de l'image de fond
- Aperçu en temps réel
- Application pixel par pixel conforme aux TPs

### Transformations
- Rotation de l'image
- Flip horizontal et vertical
- Zoom et dézoom
- Ajustement du contraste et de la luminosité
- Colorisation

### Outils de dessin
- Pinceau avec épaisseur ajustable
- Gomme
- Pipette (sélecteur de couleur)
- Sceau de peinture (pot de remplissage)
- Zone de texte

### Historique
- Annuler les actions (Undo)
- Rétablir les actions (Redo)
- Gestion complète de l'historique des modifications

## Technologies utilisées

### Langage et Build
- **Java 17** - Langage de programmation principal
- **Maven** - Gestion des dépendances et build

### Frameworks et Bibliothèques
- **Swing** - Interface graphique (GUI)
- **BufferedImage** - Manipulation d'images

### Architecture
- **Pattern MVC** - Séparation Modèle / Vue / Contrôleur
  - `Metier/` - Modèles et logique métier
  - `Vue/` - Composants graphiques et interfaces
  - `Main.java` - Contrôleur principal

## Installation et exécution

### Prérequis
- Java Development Kit (JDK) 17 ou supérieur
- Maven 3.6 ou supérieur

### Compilation
```bash
cd application
mvn clean compile
```

### Exécution
.jar

Ou via IDE (Eclipse, IntelliJ, VS Code) en exécutant `Main.java`

## Structure du projet

```
application/
├── src/
│   └── main/
│       ├── java/
│       │   └── application/multimedia/iut/
│       │       ├── Main.java                    # Point d'entrée
│       │       ├── Metier/                      # Modèles
│       │       │   ├── TexteAvecImage.java     # Texte avec image de fond
│       │       │   ├── AjoutContenu.java       # Ajout de contenu
│       │       │   ├── Colorisation.java       # Colorisation
│       │       │   └── image/                   # Gestion des couches
│       │       │       ├── PileCouches.java
│       │       │       ├── CoucheImage.java
│       │       │       ├── RenduToile.java
│       │       │       └── SessionPlacement.java
│       │       └── Vue/                         # Interface graphique
│       │           ├── PaintFrame.java          # Fenêtre principale
│       │           ├── PaintPanel.java          # Panel de dessin
│       │           ├── barres/                  # Menus et barres d'outils
│       │           ├── dialogs/                 # Dialogues
│       │           ├── panels/                  # Panneaux de configuration
│       │           └── utils/                   # Utilitaires d'interface
│       └── resources/                           # Ressources (images, icônes)
├── pom.xml                                      # Configuration Maven
└── target/                                      # Fichiers compilés
```

## Conformité académique

Ce projet essaie de respecter au mieux  les enseignements des TPs de traitement d'image vue durant le cours de Multimédia. :
- **TP1** : Sélection de territoires et interfaces personnalisées
- **TP2** : Manipulation pixel par pixel avec `getRGB()` / `setRGB()`
- **TP3** : Découpage et fusion d'images
- **TP4** : Rotation et transformations d'images

### Méthodes utilisées
- Extraction des composants RGB par décalage de bits : `(couleur >> 16) & 0xFF`
- Manipulation directe des pixels avec `BufferedImage.getRGB()` et `setRGB()`
- Redimensionnement par échantillonnage
- Fusion avec gestion du canal alpha
- Calculs de couleurs : `rouge * 256*256 + vert * 256 + bleu`

## Auteurs
Projet développé dans le cadre de la SAE - IUT du Havre - 3ème année

## Licence
Projet académique - IUT du Havre


