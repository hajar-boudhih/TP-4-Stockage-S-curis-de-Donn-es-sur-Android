# TP-4 : Stockage Sécurisé de Données sur Android

Ce projet est une application de démonstration des meilleures pratiques de stockage de données sécurisé sur Android, réalisée dans le cadre du TP-4. Elle combine un laboratoire d'exploration des différentes méthodes de stockage et une application finale de type Mini-SGBD local entièrement chiffré.

##  Fonctionnalités implémentées

### 1. Stockage de Fichiers
- **Stockage Interne** : Utilisation de `MODE_PRIVATE` pour isoler les données de l'application.
- **Stockage Externe** : Gestion du stockage spécifique à l'application avec support du Scoped Storage (Android 10+).
- **MediaStore (Scoped Storage)** : Gestion sécurisée des images dans la galerie publique sans permissions excessives.

### 2. Base de Données Sécurisée (Room + SQLCipher)
- Persistance structurée avec **Room**.
- Chiffrement intégral de la base de données via **SQLCipher** (AES-256).
- Gestion de la phrase secrète stockée dans **EncryptedSharedPreferences** (Jetpack Security).

### 3. Chiffrement Avancé
- **Jetpack Security Crypto** : Chiffrement de fichiers textes individuels avec `EncryptedFile`.
- **Android Keystore** : Protection des clés cryptographiques au niveau matériel.
- **StrongBox** : Support des puces de sécurité matérielles dédiées si disponibles.
- **PBKDF2** : Dérivation de clés et hachage de mots de passe (10 000 itérations + Sel).

### 4. Analyse de Sécurité
- Détection automatique des risques : backups activés, mode debug, absence de StrongBox, exécution sur émulateur.

### 5. Projet Final : Mini-SGBD
- Système de Login/Registre sécurisé.
- Journalisation (Audit Logs) chiffrée de toutes les actions utilisateur.

---

## 📸 Captures d'écran

| Écran de Connexion | Laboratoire de Sécurité | Liste des Notes |
|:---:|:---:|:---:|
| ![Connexion](screenshots/login.png) | ![Labo](screenshots/main_lab.png) | ![Notes](screenshots/notes_list.png) |

*(Note : Remplacez les chemins ci-dessus par vos propres captures d'écran dans un dossier `screenshots/`)*

---

## 🛠 Configuration et Installation

### Prérequis
- Android Studio Ladybug ou version supérieure.
- Android SDK 24 (Min) / 36 (Target).

### Dépendances principales
- `androidx.security:security-crypto:1.1.0-alpha06`
- `net.zetetic:android-database-sqlcipher:4.5.3`
- `androidx.room:room-runtime:2.5.2`
- `com.google.crypto.tink:tink-android:1.8.0`

---

##  Étapes du TP

### Étape 1 : Configuration
Ajout des permissions dans le `AndroidManifest.xml` (limitation des permissions externes aux anciennes versions d'Android) et configuration du `build.gradle`.

### Étape 2 : Gestionnaires de Stockage
Création de `InternalStorageManager`, `ExternalStorageManager` et `MediaStoreManager`.

### Étape 3 : Sécurisation de la BDD
Implémentation de Room avec le support de SQLCipher. Utilisation de `EncryptedSharedPreferences` pour protéger la clé de chiffrement.

### Étape 4 : Chiffrement de Fichiers
Utilisation de `EncryptedFile` pour stocker des données sensibles hors base de données.

### Étape 5 : Analyseur de Risques
Implémentation de `SecurityAnalyzer` pour auditer la configuration de sécurité de l'application.

### Étape 6 : Projet Final
Fusion de toutes les briques pour créer une application de gestion de notes sécurisée avec audit logs.

---

##  Auteur
**Hajar BOUDHIH**
[Lien vers le dépôt GitHub](https://github.com/hajar-boudhih/TP-4-Stockage-S-curis-de-Donn-es-sur-Android.git)
