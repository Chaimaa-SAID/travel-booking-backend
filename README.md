# Travel Booking Backend - Microservices

Plateforme de réservation de voyages centralisée avec architecture microservices, permettant la gestion des vols, hôtels, utilisateurs, paiements et réservations.

## Vue d'ensemble
**Concept** : Permettre aux utilisateurs de rechercher et réserver des vols et hôtels via une interface unifiée. Gestion des rôles : `ADMIN` et `USER`.  
**Durée** : Projet académique, 3 mois  
**Statut** : Services backend principaux développés, communication inter-services opérationnelle

---

## Architecture Microservices

### Services d'infrastructure
| Service           | Port  | Statut | Description                       |
|------------------|-------|--------|-----------------------------------|
| Discovery Server | 8761  | ✅      | Découverte des services (Eureka) |
| API Gateway      | 8080  | ✅      | Point d’entrée, routage           |

### Services métier
| Service           | Port  | Statut | Description                                      |
|------------------|-------|--------|-------------------------------------------------|
| User Service      | 8084  | ✅      | Gestion des utilisateurs (ADMIN, USER)         |
| Flight Service    | 8083  | ✅      | Gestion des vols                                |
| Hotel Service     | 8082  | ✅      | Gestion des hôtels                              |
| Booking Service   | 8081  | ✅      | Gestion des réservations                        |
| Payment Service   | 8085  | ✅      | Gestion des paiements                            |
| Vol Service       | 8086  | 🔨      | Service complémentaire / extensions             |

**Rôles et permissions** :
- `ADMIN` : Ajouter/Modifier/Supprimer utilisateurs, vols, hôtels, gérer paiements et réservations  
- `USER` : Consulter vols/hôtels, créer réservations, payer ses réservations  

---

## Démarrage rapide

1. **Lancer tous les services**  
   Depuis le terminal dans le dossier racine :
   ```bash
   cd api-gateway
   mvn spring-boot:run
   cd ../discovery-server
   mvn spring-boot:run
