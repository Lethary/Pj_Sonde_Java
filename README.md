# 📌 Pj_Sonde – Application de gestion de sondes de température

Pj_Sonde est une application lourde développée en **Java Swing** avec une architecture **MVC**.  
Elle permet la gestion complète des **bâtiments**, **salles** et **sondes**, ainsi que la préparation à l’intégration future de relevés de température via une API.

Une **interface web en Laravel** sera développée dans la suite du projet pour offrir un accès en ligne à la gestion et à la consultation des données.

---

## 🎯 Objectifs du projet

- Gérer les **bâtiments** (CRUD)
- Gérer les **salles**, liées à un bâtiment (CRUD)
- Gérer les **sondes**, liées à une salle (CRUD)
- Structurer l’application en **MVC**
- Préparer l’intégration d’une API (PRTG) pour récupérer les mesures de température
- Mettre en place une **future interface web (Laravel)** pour consultation et gestion avancée
- Utiliser un **Kanban GitHub** pour organiser le développement


---

## 📊 MCD utilisé pour le projet

Le projet repose sur un MCD contenant les entités suivantes :

- **Bâtiments**
- **Salles**
- **Sondes**
- **Types de sondes**
- **Unités**
- **Relevés** (prévu pour la suite)
- **Utilisateurs** et **Rôles** (prévu pour extension)
- **Association des sondes à des salles et bâtiments**

---

## 🧱 Fonctionnalités actuelles

- Structure complète des **modèles**
- Développement des **vues principales**
- CRUD en cours d’implémentation :
  - Bâtiments  
  - Salles  
  - Sondes

---

## 🗂️ Organisation du projet

Le développement est géré via un **tableau Kanban GitHub**, composé de :

- **Idée**  
- **Prêt à être réalisé**  
- **En cours**  
- **En train d’être testé**  
- **Fini**

Chaque fonctionnalité (ajout, modification, suppression) correspond à une issue claire et isolée.

---

## 🛠️ Technologies utilisées

- **Java 21**
- **Swing**
- **Pattern MVC**
- **Git / GitHub**
- **Kanban GitHub**
- **Laravel** (pour la future interface web)

---

## 🚀 Prochaines étapes

- Finalisation des CRUD Bâtiment / Salle / Sonde  
- Lier les salles à un bâtiment  
- Lier les sondes à une salle  
- Intégration de l’API PRTG pour les relevés  
- Ajout d’une interface graphique pour afficher les relevés  
- Création d’une **interface web Laravel** pour la consultation en ligne  
- Ajout de tests unitaires  

---

## 👤 Auteur

Projet développé par **Lethary** dans le cadre d’un projet Java/MVC complet.

