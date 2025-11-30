/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.controller;

import com.example.centre.entity.CampagneDon;
import com.example.centre.repository.CampagneDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/comptable")
@CrossOrigin(origins = "*")
public class CampagneDonController {

    @Autowired
    private CampagneDonRepository campagneDonRepository;

    @PostMapping("/campagnes-dons")
    public ResponseEntity<?> createCampagneDon(@RequestBody CampagneDon campagneDon) {
        try {
            CampagneDon savedCampagne = campagneDonRepository.save(campagneDon);
            return ResponseEntity.ok(savedCampagne);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création de la campagne de dons: " + e.getMessage());
        }
    }

    @GetMapping("/campagnes-dons")
    public List<CampagneDon> getAllCampagnesDons() {
        return campagneDonRepository.findAll();
    }

    @GetMapping("/campagnes-dons/{id}")
    public ResponseEntity<?> getCampagneDonById(@PathVariable Long id) {
        Optional<CampagneDon> campagne = campagneDonRepository.findById(id);
        return campagne.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/campagnes-dons/{id}")
    public ResponseEntity<?> updateCampagneDon(@PathVariable Long id, @RequestBody CampagneDon campagneDetails) {
        Optional<CampagneDon> campagneOpt = campagneDonRepository.findById(id);
        if (campagneOpt.isPresent()) {
            CampagneDon campagne = campagneOpt.get();
            campagne.setTitre(campagneDetails.getTitre());
            campagne.setDescription(campagneDetails.getDescription());
            campagne.setObjectif(campagneDetails.getObjectif());
            campagne.setDateDebut(campagneDetails.getDateDebut());
            campagne.setDateFin(campagneDetails.getDateFin());
            campagne.setObjectifMontant(campagneDetails.getObjectifMontant());
            campagne.setImageUrl(campagneDetails.getImageUrl());
            campagne.setCouleurTheme(campagneDetails.getCouleurTheme());
            campagne.setMessageRemerciement(campagneDetails.getMessageRemerciement());
            
            CampagneDon updatedCampagne = campagneDonRepository.save(campagne);
            return ResponseEntity.ok(updatedCampagne);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/campagnes-dons/{id}")
    public ResponseEntity<?> deleteCampagneDon(@PathVariable Long id) {
        if (campagneDonRepository.existsById(id)) {
            campagneDonRepository.deleteById(id);
            return ResponseEntity.ok("Campagne de dons supprimée avec succès");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/campagnes-dons/actives")
    public List<CampagneDon> getCampagnesActives() {
        return campagneDonRepository.findByActiveTrue();
    }

    @GetMapping("/campagnes-dons/en-cours")
    public List<CampagneDon> getCampagnesEnCours() {
        return campagneDonRepository.findCampagnesEnCours(LocalDate.now());
    }

    @GetMapping("/campagnes-dons/terminees")
    public List<CampagneDon> getCampagnesTerminees() {
        return campagneDonRepository.findCampagnesTerminees(LocalDate.now());
    }

    @GetMapping("/campagnes-dons/a-venir")
    public List<CampagneDon> getCampagnesAVenir() {
        return campagneDonRepository.findCampagnesAVenir(LocalDate.now());
    }

    @GetMapping("/campagnes-dons/objectif-atteint")
    public List<CampagneDon> getCampagnesObjectifAtteint() {
        return campagneDonRepository.findCampagnesObjectifAtteint();
    }

    @PutMapping("/campagnes-dons/{id}/cloturer")
    public ResponseEntity<?> cloturerCampagne(@PathVariable Long id) {
        Optional<CampagneDon> campagneOpt = campagneDonRepository.findById(id);
        if (campagneOpt.isPresent()) {
            CampagneDon campagne = campagneOpt.get();
            campagne.cloturerCampagne();
            campagneDonRepository.save(campagne);
            return ResponseEntity.ok("Campagne clôturée");
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/campagnes-dons/{id}/reactiver")
    public ResponseEntity<?> reactiverCampagne(@PathVariable Long id) {
        Optional<CampagneDon> campagneOpt = campagneDonRepository.findById(id);
        if (campagneOpt.isPresent()) {
            CampagneDon campagne = campagneOpt.get();
            campagne.reactiverCampagne();
            campagneDonRepository.save(campagne);
            return ResponseEntity.ok("Campagne réactivée");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/campagnes-dons/recherche")
    public List<CampagneDon> rechercherCampagnes(@RequestParam String keyword) {
        return campagneDonRepository.searchByKeyword(keyword);
    }

    @GetMapping("/campagnes-dons/{id}/statistiques")
    public ResponseEntity<?> getStatistiquesCampagne(@PathVariable Long id) {
        Optional<CampagneDon> campagneOpt = campagneDonRepository.findById(id);
        if (campagneOpt.isPresent()) {
            CampagneDon campagne = campagneOpt.get();
            Map<String, Object> stats = Map.of(
                "titre", campagne.getTitre(),
                "objectif", campagne.getObjectifMontant(),
                "collecte", campagne.getMontantCollecte(),
                "progression", campagne.calculerProgression(),
                "nombreDons", campagne.getNombreDons(),
                "joursRestants", campagne.getJoursRestants(),
                "statut", campagne.estEnCours() ? "En cours" : campagne.estTerminee() ? "Terminée" : "À venir"
            );
            return ResponseEntity.ok(stats);
        }
        return ResponseEntity.notFound().build();
    }
}