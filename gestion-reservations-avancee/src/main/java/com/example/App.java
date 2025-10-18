package com.example;

import com.example.model.Equipement;
import com.example.model.Reservation;
import com.example.model.Salle;
import com.example.model.Utilisateur;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {

    public static void main(String[] args) {
        // Création de l'EntityManagerFactory
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestion-reservations");
        EntityManager em = emf.createEntityManager();

        try {
            // Initialisation des données de test
            initializeTestData(em);

            // Test 1: Recherche de salles disponibles par créneau
            System.out.println("\n=== Test 1: Recherche de salles disponibles par créneau ===");
            testAvailableRooms(em);

            // Test 2: Recherche multi-critères
            System.out.println("\n=== Test 2: Recherche multi-critères ===");
            testMultiCriteriaSearch(em);

        } finally {
            em.close();
            emf.close();
        }
    }

    private static void initializeTestData(EntityManager em) {
        em.getTransaction().begin();

        // Création des équipements
        Equipement projecteur = new Equipement("Projecteur", "Projecteur HD");
        Equipement ecran = new Equipement("Écran interactif", "Écran tactile 65 pouces");
        Equipement visioconference = new Equipement("Système de visioconférence", "Caméra HD + micro");

        em.persist(projecteur);
        em.persist(ecran);
        em.persist(visioconference);

        // Création des utilisateurs
        Utilisateur user1 = new Utilisateur("Dupont", "Jean", "jean.dupont@example.com");
        Utilisateur user2 = new Utilisateur("Martin", "Sophie", "sophie.martin@example.com");

        em.persist(user1);
        em.persist(user2);

        // Création des salles
        Salle salle1 = new Salle("Salle A101", 30);
        salle1.setDescription("Salle de réunion standard");
        salle1.setBatiment("Bâtiment A");
        salle1.setEtage(1);
        salle1.addEquipement(projecteur);

        Salle salle2 = new Salle("Salle B202", 15);
        salle2.setDescription("Petite salle de réunion");
        salle2.setBatiment("Bâtiment B");
        salle2.setEtage(2);
        salle2.addEquipement(ecran);

        Salle salle3 = new Salle("Salle C303", 50);
        salle3.setDescription("Grande salle de conférence");
        salle3.setBatiment("Bâtiment C");
        salle3.setEtage(3);
        salle3.addEquipement(projecteur);
        salle3.addEquipement(visioconference);

        Salle salle4 = new Salle("Salle A202", 20);
        salle4.setDescription("Salle de formation");
        salle4.setBatiment("Bâtiment A");
        salle4.setEtage(2);
        salle4.addEquipement(projecteur);
        salle4.addEquipement(ecran);

        Salle salle5 = new Salle("Salle B303", 40);
        salle5.setDescription("Salle polyvalente");
        salle5.setBatiment("Bâtiment B");
        salle5.setEtage(3);
        salle5.addEquipement(visioconference);

        em.persist(salle1);
        em.persist(salle2);
        em.persist(salle3);
        em.persist(salle4);
        em.persist(salle5);

        // Création des réservations
        LocalDateTime now = LocalDateTime.now();

        Reservation res1 = new Reservation(
                now.plusDays(1).withHour(9).withMinute(0),
                now.plusDays(1).withHour(11).withMinute(0),
                "Réunion d'équipe"
        );
        res1.setUtilisateur(user1);
        res1.setSalle(salle1);

        Reservation res2 = new Reservation(
                now.plusDays(2).withHour(14).withMinute(0),
                now.plusDays(2).withHour(16).withMinute(0),
                "Entretien"
        );
        res2.setUtilisateur(user2);
        res2.setSalle(salle2);

        Reservation res3 = new Reservation(
                now.plusDays(3).withHour(10).withMinute(0),
                now.plusDays(3).withHour(12).withMinute(0),
                "Présentation client"
        );
        res3.setUtilisateur(user1);
        res3.setSalle(salle3);

        em.persist(res1);
        em.persist(res2);
        em.persist(res3);

        em.getTransaction().commit();
        System.out.println("Données de test initialisées avec succès !");
    }

    private static void testAvailableRooms(EntityManager em) {
        // Exemple simple: afficher toutes les salles
        List<Salle> salles = em.createQuery("SELECT s FROM Salle s", Salle.class).getResultList();
        System.out.println("Toutes les salles :");
        for (Salle salle : salles) {
            System.out.println("- " + salle.getNom() + " (" + salle.getCapacite() + " places)");
        }
    }

    private static void testMultiCriteriaSearch(EntityManager em) {
        // Exemple simple: recherche par bâtiment
        String batimentRecherche = "Bâtiment A";
        List<Salle> salles = em.createQuery("SELECT s FROM Salle s WHERE s.batiment = :batiment", Salle.class)
                .setParameter("batiment", batimentRecherche)
                .getResultList();

        System.out.println("\nSalles dans le " + batimentRecherche + " :");
        for (Salle salle : salles) {
            System.out.println("- " + salle.getNom() + " (" + salle.getCapacite() + " places)");
        }
    }
}
