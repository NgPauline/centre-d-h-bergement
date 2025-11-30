/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.controller;

import com.example.centre.entity.Facture;
import com.example.centre.entity.StatutFacture;
import com.example.centre.repository.FactureRepository;
import com.example.centre.repository.ResidentRepository;
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
public class FactureController {

    @Autowired
    private FactureRepository factureRepository;

    @Autowired
    private ResidentRepository residentRepository;

    @PostMapping("/factures")
    public ResponseEntity<?> createFacture(@RequestBody Facture facture) {
        try {
            // Vérifier si le résident existe
            if (!residentRepository.existsById(facture.getResident().getId())) {
                return ResponseEntity.badRequest().body("Résident non trouvé");
            }

            // Générer un numéro de facture unique
            String numeroFacture = "FAC-" + System.currentTimeMillis();
            facture.setNumero(numeroFacture);
            
            // Calculer le total
            facture.calculerTotal();
            
            Facture savedFacture = factureRepository.save(facture);
            return ResponseEntity.ok(savedFacture);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création de la facture: " + e.getMessage());
        }
    }

    @GetMapping("/factures")
    public List<Facture> getAllFactures() {
        return factureRepository.findAll();
    }

    @GetMapping("/factures/{id}")
    public ResponseEntity<?> getFactureById(@PathVariable Long id) {
        Optional<Facture> facture = factureRepository.findById(id);
        return facture.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/factures/{id}")
    public ResponseEntity<?> updateFacture(@PathVariable Long id, @RequestBody Facture factureDetails) {
        Optional<Facture> factureOpt = factureRepository.findById(id);
        if (factureOpt.isPresent()) {
            Facture facture = factureOpt.get();
            facture.setDateEmission(factureDetails.getDateEmission());
            facture.setDateEcheance(factureDetails.getDateEcheance());
            facture.setNotes(factureDetails.getNotes());
            
            Facture updatedFacture = factureRepository.save(facture);
            return ResponseEntity.ok(updatedFacture);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/factures/{id}")
    public ResponseEntity<?> deleteFacture(@PathVariable Long id) {
        if (factureRepository.existsById(id)) {
            factureRepository.deleteById(id);
            return ResponseEntity.ok("Facture supprimée avec succès");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/factures/resident/{residentId}")
    public List<Facture> getFacturesByResident(@PathVariable Long residentId) {
        return factureRepository.findByResidentId(residentId);
    }

    @GetMapping("/factures/statut/{statut}")
    public List<Facture> getFacturesByStatut(@PathVariable StatutFacture statut) {
        return factureRepository.findByStatut(statut);
    }

    @GetMapping("/factures/en-retard")
    public List<Facture> getFacturesEnRetard() {
        return factureRepository.findFacturesEnRetard(LocalDate.now());
    }

    @PutMapping("/factures/{id}/marquer-payee")
    public ResponseEntity<?> marquerFacturePayee(@PathVariable Long id) {
        Optional<Facture> factureOpt = factureRepository.findById(id);
        if (factureOpt.isPresent()) {
            Facture facture = factureOpt.get();
            facture.marquerPayee();
            factureRepository.save(facture);
            return ResponseEntity.ok("Facture marquée comme payée");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/factures/stats/chiffre-affaire")
    public ResponseEntity<?> getChiffreAffaire(@RequestParam String startDate, @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        
        Double total = factureRepository.findTotalFacturesPayeesPeriode(start, end);
        return ResponseEntity.ok(Map.of("total", total != null ? total : 0.0));
    }

    @GetMapping("/factures/numero/{numero}")
    public ResponseEntity<?> getFactureByNumero(@PathVariable String numero) {
        Optional<Facture> facture = factureRepository.findByNumero(numero);
        return facture.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
}
