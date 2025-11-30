/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.controller;

import com.example.centre.entity.StatutResident;
import com.example.centre.entity.Role;
import com.example.centre.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private ChambreRepository chambreRepository;

    @Autowired
    private PersonnelRepository personnelRepository;

    @Autowired
    private FactureRepository factureRepository;

    @Autowired
    private DonRepository donRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private DossierMedicalRepository dossierMedicalRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private ActiviteRepository activiteRepository;

    @Autowired
    private NoteQuotidienneRepository noteQuotidienneRepository;

    // 🔹 MÉTHODE UNIQUE POUR TOUS LES RÔLES
    @GetMapping("/stats")
    public ResponseEntity<?> getDashboardStats() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        Role userRole = utilisateurRepository.findByUsername(username)
            .map(user -> user.getRole())
            .orElse(Role.SECRETAIRE);

        Map<String, Object> stats = new HashMap<>();
        stats.put("role", userRole.name());
        stats.put("username", username);

        switch (userRole) {
            case ADMIN:
            case DIRECTEUR:
                return ResponseEntity.ok(getStatsDirecteur());
            case INFIRMIER:
                return ResponseEntity.ok(getStatsInfirmier());
            case AIDE_SOIGNANT:
                return ResponseEntity.ok(getStatsAideSoignant());
            case EDUCATEUR:
                return ResponseEntity.ok(getStatsEducateur());
            case COMPTABLE:
                return ResponseEntity.ok(getStatsComptable());
            case SECRETAIRE:
                return ResponseEntity.ok(getStatsSecretaire());
            default:
                return ResponseEntity.ok(getStatsDefault());
        }
    }

    // 🔹 DASHBOARD ADMIN/DIRECTEUR (fusionné) - CHANGÉ EN PUBLIC
    public Map<String, Object> getStatsDirecteur() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            // Résidents
            stats.put("totalResidents", residentRepository.count());
            stats.put("residentsActifs", residentRepository.countByStatut(StatutResident.ACTIF));
            
            // Utiliser les nouvelles méthodes
            stats.put("residentsBesoinInfirmier", residentRepository.countByBesoinInfirmier(true));
            stats.put("residentsBesoinAideSoignant", residentRepository.countByBesoinAideSoignant(true));
            
            // Chambres
            stats.put("totalChambres", chambreRepository.count());
            stats.put("chambresDisponibles", residentRepository.countChambresDisponibles());
            
            long chambresPleines = chambreRepository.findAll().stream()
                .filter(c -> c.getResidents().size() >= c.getCapacite())
                .count();
            stats.put("chambresOccupees", chambresPleines);
            
            // Personnel
            stats.put("totalPersonnel", personnelRepository.count());
            
            // Finances
            stats.put("facturesEnRetard", factureRepository.countFacturesEnRetard());
            
            // Dons du mois
            double donsMois = donRepository.findAll().stream()
                .filter(d -> d.getDateDon() != null && 
                           d.getDateDon().getMonth() == LocalDate.now().getMonth() &&
                           d.getDateDon().getYear() == LocalDate.now().getYear())
                .mapToDouble(d -> d.getMontant() != null ? d.getMontant() : 0.0)
                .sum();
            stats.put("donsMois", donsMois);
            
        } catch (Exception e) {
            setValeursParDefaut(stats);
        }
        
        return stats;
    }

    // 🔹 DASHBOARD INFIRMIER - CHANGÉ EN PUBLIC
    public Map<String, Object> getStatsInfirmier() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            // Résidents sous sa responsabilité
            stats.put("mesResidents", residentRepository.countByBesoinInfirmier(true));
            
            // Soins en attente - utiliser la méthode estActive()
            long prescriptionsEnAttente = prescriptionRepository.findAll().stream()
                .filter(p -> p.estActive())
                .count();
            stats.put("prescriptionsEnAttente", prescriptionsEnAttente);
            
            // Notes médicales aujourd'hui
            stats.put("notesAujourdhui", noteQuotidienneRepository.countNotesToday());
            
            // Résidents avec besoins spécifiques
            stats.put("residentsAllergies", countResidentsAvecAllergies());
            stats.put("residentsTraitements", countResidentsAvecTraitements());
            
        } catch (Exception e) {
            setValeursInfirmier(stats);
        }
        
        return stats;
    }

    // 🔹 DASHBOARD AIDE-SOIGNANT - CHANGÉ EN PUBLIC
    public Map<String, Object> getStatsAideSoignant() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            stats.put("mesResidents", residentRepository.countByBesoinAideSoignant(true));
            stats.put("toilettesEnAttente", residentRepository.countByBesoinAideSoignant(true));
            stats.put("repasAServir", residentRepository.countByStatut(StatutResident.ACTIF));
            stats.put("mobilisationsPrevues", 3);
            stats.put("notesAujourdhui", noteQuotidienneRepository.countNotesToday());
            
        } catch (Exception e) {
            setValeursAideSoignant(stats);
        }
        
        return stats;
    }

    // 🔹 DASHBOARD ÉDUCATEUR - CHANGÉ EN PUBLIC
    public Map<String, Object> getStatsEducateur() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            stats.put("activitesAujourdhui", activiteRepository.countActivitesToday());
            stats.put("activitesSemaine", activiteRepository.countActivitesThisWeek());
            stats.put("totalParticipants", activiteRepository.countTotalParticipantsToday());
            stats.put("programmesEnCours", 5);
            stats.put("evaluationsEnAttente", 3);
            
        } catch (Exception e) {
            setValeursEducateur(stats);
        }
        
        return stats;
    }

    // 🔹 DASHBOARD COMPTABLE - CHANGÉ EN PUBLIC
    public Map<String, Object> getStatsComptable() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            stats.put("facturesEnRetard", factureRepository.countFacturesEnRetard());
            stats.put("facturesMois", factureRepository.countFacturesThisMonth());
            
            double totalImpayes = factureRepository.findAll().stream()
                .filter(f -> f.getStatut() == com.example.centre.entity.StatutFacture.EN_RETARD)
                .mapToDouble(f -> f.getMontantTotal() != null ? f.getMontantTotal() : 0.0)
                .sum();
            stats.put("totalImpayes", totalImpayes);
            
            double donsMois = donRepository.findAll().stream()
                .filter(d -> d.getDateDon() != null && 
                           d.getDateDon().getMonth() == LocalDate.now().getMonth() &&
                           d.getDateDon().getYear() == LocalDate.now().getYear())
                .mapToDouble(d -> d.getMontant() != null ? d.getMontant() : 0.0)
                .sum();
            stats.put("donsMois", donsMois);
            
            stats.put("paiementsAttente", factureRepository.findAll().stream()
                .filter(f -> f.getStatut() == com.example.centre.entity.StatutFacture.EMISE)
                .count());
            
        } catch (Exception e) {
            setValeursComptable(stats);
        }
        
        return stats;
    }

    // 🔹 DASHBOARD SECRETAIRE - CHANGÉ EN PUBLIC
    public Map<String, Object> getStatsSecretaire() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            stats.put("totalResidents", residentRepository.count());
            stats.put("nouveauxResidentsMois", residentRepository.countNewResidentsThisMonth());
            stats.put("documentsAValider", 3);
            stats.put("rendezVousAujourdhui", 5);
            stats.put("courriersEnvoyer", 2);
            
        } catch (Exception e) {
            setValeursSecretaire(stats);
        }
        
        return stats;
    }

    // 🔹 DASHBOARD PAR DÉFAUT - CHANGÉ EN PUBLIC
    public Map<String, Object> getStatsDefault() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("message", "Tableau de bord en cours de configuration pour votre rôle");
        stats.put("totalResidents", residentRepository.count());
        return stats;
    }

    // Méthodes utilitaires - LAISSÉES EN PRIVATE
    private long countResidentsAvecAllergies() {
        try {
            return dossierMedicalRepository.findAll().stream()
                .filter(dm -> dm.getAllergies() != null && !dm.getAllergies().isEmpty())
                .count();
        } catch (Exception e) {
            return 0;
        }
    }

    private long countResidentsAvecTraitements() {
        try {
            return prescriptionRepository.findAll().stream()
                .filter(p -> p.estActive())
                .map(p -> p.getResident())
                .distinct()
                .count();
        } catch (Exception e) {
            return 0;
        }
    }

    private void setValeursParDefaut(Map<String, Object> stats) {
        stats.put("totalResidents", 0);
        stats.put("residentsActifs", 0);
        stats.put("residentsBesoinInfirmier", 0);
        stats.put("residentsBesoinAideSoignant", 0);
        stats.put("totalChambres", 0);
        stats.put("chambresDisponibles", 0);
        stats.put("chambresOccupees", 0);
        stats.put("totalPersonnel", 0);
        stats.put("facturesEnRetard", 0);
        stats.put("donsMois", 0.0);
    }

    private void setValeursInfirmier(Map<String, Object> stats) {
        stats.put("mesResidents", 0);
        stats.put("prescriptionsEnAttente", 0);
        stats.put("notesAujourdhui", 0);
        stats.put("residentsAllergies", 0);
        stats.put("residentsTraitements", 0);
    }

    private void setValeursAideSoignant(Map<String, Object> stats) {
        stats.put("mesResidents", 0);
        stats.put("toilettesEnAttente", 0);
        stats.put("repasAServir", 0);
        stats.put("mobilisationsPrevues", 0);
        stats.put("notesAujourdhui", 0);
    }

    private void setValeursEducateur(Map<String, Object> stats) {
        stats.put("activitesAujourdhui", 0);
        stats.put("activitesSemaine", 0);
        stats.put("totalParticipants", 0);
        stats.put("programmesEnCours", 0);
        stats.put("evaluationsEnAttente", 0);
    }

    private void setValeursComptable(Map<String, Object> stats) {
        stats.put("facturesEnRetard", 0);
        stats.put("totalImpayes", 0.0);
        stats.put("facturesMois", 0);
        stats.put("donsMois", 0.0);
        stats.put("paiementsAttente", 0);
    }

    private void setValeursSecretaire(Map<String, Object> stats) {
        stats.put("totalResidents", 0);
        stats.put("nouveauxResidentsMois", 0);
        stats.put("documentsAValider", 0);
        stats.put("rendezVousAujourdhui", 0);
        stats.put("courriersEnvoyer", 0);
    }
}