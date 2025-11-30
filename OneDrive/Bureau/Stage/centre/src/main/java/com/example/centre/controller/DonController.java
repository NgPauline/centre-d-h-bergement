/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.controller;

import com.example.centre.entity.Don;
import com.example.centre.entity.TypeDon;
import com.example.centre.entity.TypeDonateur;
import com.example.centre.repository.DonRepository;
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
public class DonController {

    @Autowired
    private DonRepository donRepository;

    @PostMapping("/dons")
    public ResponseEntity<?> createDon(@RequestBody Don don) {
        try {
            // Générer un numéro de don unique
            String numeroDon = "DON-" + System.currentTimeMillis();
            don.setNumeroDon(numeroDon);
            
            Don savedDon = donRepository.save(don);
            return ResponseEntity.ok(savedDon);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de l'enregistrement du don: " + e.getMessage());
        }
    }

    @GetMapping("/dons")
    public List<Don> getAllDons() {
        return donRepository.findAll();
    }

    @GetMapping("/dons/{id}")
    public ResponseEntity<?> getDonById(@PathVariable Long id) {
        Optional<Don> don = donRepository.findById(id);
        return don.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/dons/{id}")
    public ResponseEntity<?> updateDon(@PathVariable Long id, @RequestBody Don donDetails) {
        Optional<Don> donOpt = donRepository.findById(id);
        if (donOpt.isPresent()) {
            Don don = donOpt.get();
            don.setNomDonateur(donDetails.getNomDonateur());
            don.setPrenomDonateur(donDetails.getPrenomDonateur());
            don.setOrganisationDonateur(donDetails.getOrganisationDonateur());
            don.setEmail(donDetails.getEmail());
            don.setTelephone(donDetails.getTelephone());
            don.setMontant(donDetails.getMontant());
            don.setTypeDon(donDetails.getTypeDon());
            don.setTypeDonateur(donDetails.getTypeDonateur());
            don.setMessage(donDetails.getMessage());
            
            Don updatedDon = donRepository.save(don);
            return ResponseEntity.ok(updatedDon);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/dons/{id}")
    public ResponseEntity<?> deleteDon(@PathVariable Long id) {
        if (donRepository.existsById(id)) {
            donRepository.deleteById(id);
            return ResponseEntity.ok("Don supprimé avec succès");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/dons/type-donateur/{typeDonateur}")
    public List<Don> getDonsByTypeDonateur(@PathVariable TypeDonateur typeDonateur) {
        return donRepository.findByTypeDonateur(typeDonateur);
    }

    @GetMapping("/dons/type-don/{typeDon}")
    public List<Don> getDonsByTypeDon(@PathVariable TypeDon typeDon) {
        return donRepository.findByTypeDon(typeDon);
    }

    @GetMapping("/dons/recus-fiscaux/en-attente")
    public List<Don> getDonsAvecRecusEnAttente() {
        return donRepository.findByRecuFiscalEnvoyeFalse();
    }

    @PutMapping("/dons/{id}/generer-recu-fiscal")
    public ResponseEntity<?> genererRecuFiscal(@PathVariable Long id) {
        Optional<Don> donOpt = donRepository.findById(id);
        if (donOpt.isPresent()) {
            Don don = donOpt.get();
            String numeroRecu = don.genererRecuFiscal();
            donRepository.save(don);
            return ResponseEntity.ok(Map.of("numeroRecu", numeroRecu));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/dons/{id}/envoyer-remerciement")
    public ResponseEntity<?> envoyerRemerciement(@PathVariable Long id) {
        Optional<Don> donOpt = donRepository.findById(id);
        if (donOpt.isPresent()) {
            Don don = donOpt.get();
            don.envoyerRemerciement();
            donRepository.save(don);
            return ResponseEntity.ok("Remerciement envoyé au donateur");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/dons/stats/total")
    public ResponseEntity<?> getTotalDons(@RequestParam(required = false) String startDate, 
                                         @RequestParam(required = false) String endDate) {
        Double total;
        if (startDate != null && endDate != null) {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            total = donRepository.findTotalDonsPeriode(start, end);
        } else {
            // Total de tous les dons
            total = donRepository.findAll().stream()
                    .mapToDouble(Don::getMontant)
                    .sum();
        }
        
        return ResponseEntity.ok(Map.of("total", total != null ? total : 0.0));
    }

    @GetMapping("/dons/recherche")
    public List<Don> rechercherDons(@RequestParam String keyword) {
        return donRepository.findByDonateurContaining(keyword);
    }
}