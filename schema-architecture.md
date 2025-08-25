# schema-architecture.md

## Section 1 : Résumé de l’architecture

L’application de gestion de clinique intelligente repose sur **Spring Boot** et combine deux approches complémentaires : des contrôleurs **Spring MVC avec Thymeleaf** pour les tableaux de bord administrateur et médecin, ainsi que des **API REST** pour les modules patients, rendez-vous et prescriptions. Cette hybridation permet à la fois de fournir des vues serveur classiques et des services modulaires consommables par des applications mobiles ou web externes.

Au niveau des données, le système exploite deux bases : **MySQL**, via **Spring Data JPA**, pour les entités relationnelles structurées (patients, médecins, rendez-vous, administrateurs), et **MongoDB**, via **Spring Data MongoDB**, pour les prescriptions flexibles stockées sous forme de documents. Toutes les requêtes, qu’elles proviennent de MVC ou de REST, transitent par une **couche de service centralisée** qui applique les règles métier, avant d’interagir avec la couche de dépôt appropriée. Cette architecture garantit une séparation claire des responsabilités, une extensibilité accrue et une meilleure maintenabilité.

---

## Section 2 : Flux numéroté de données et de contrôle

1. **Interaction utilisateur** : L’utilisateur accède à l’application via un tableau de bord Thymeleaf (AdminDashboard, DoctorDashboard) ou via un client API REST (applications mobiles ou modules frontend comme PatientDashboard).
2. **Routage de la requête** : La requête est dirigée vers le contrôleur approprié : un **contrôleur MVC** pour générer une page HTML dynamique, ou un **contrôleur REST** pour renvoyer une réponse JSON.
3. **Appel du service** : Le contrôleur délègue le traitement à la **couche de service**, qui centralise la logique métier et applique les règles (ex. : vérification de la disponibilité d’un médecin avant une prise de rendez-vous).
4. **Interaction avec les dépôts** : La couche de service appelle la **couche de référentiel** correspondante pour accéder aux données.
5. **Accès aux bases de données** :
   - Les **référentiels JPA/MySQL** gèrent les données relationnelles (patients, médecins, rendez-vous, administrateurs).
   - Les **référentiels MongoDB** gèrent les données documentaires flexibles (prescriptions).
6. **Liaison de modèle** : Les données issues des bases sont transformées en objets Java : entités JPA pour MySQL, documents annotés pour MongoDB.
7. **Retour des résultats** :
   - Dans le flux **MVC**, les modèles sont injectés dans les templates Thymeleaf et rendus en HTML pour le navigateur.
   - Dans le flux **REST**, les modèles (ou DTOs) sont sérialisés en JSON et renvoyés dans la réponse HTTP.
