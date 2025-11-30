/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.controller;

import com.example.centre.entity.Contrat;
import com.example.centre.repository.ContratRepository;
import com.example.centre.repository.ResidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/directeur")
@CrossOrigin(origins = "*")
public class ContratController {

    @Autowired
    private ContratRepository contratRepository;

    @Autowired
    private ResidentRepository residentRepository;

    @PostMapping("/contrats")
    public ResponseEntity<?> createContrat(@RequestBody Contrat contrat) {
        try {
            // Vérifier si le résident existe
            if (!residentRepository.existsById(contrat.getResident().getId())) {
                return ResponseEntity.badRequest().body("Résident non trouvé");
            }

            // Vérifier si un contrat existe déjà pour ce résident
            Optional<Contrat> existingContrat = contratRepository.findByResidentId(contrat.getResident().getId());
            if (existingContrat.isPresent()) {
                return ResponseEntity.badRequest().body("Un contrat existe déjà pour ce résident");
            }

            Contrat savedContrat = contratRepository.save(contrat);
            return ResponseEntity.ok(savedContrat);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création du contrat: " + e.getMessage());
        }
    }

    @GetMapping("/contrats")
    public List<Contrat> getAllContrats() {
        return contratRepository.findAll();
    }

    @GetMapping("/contrats/{id}")
    public ResponseEntity<?> getContratById(@PathVariable Long id) {
        Optional<Contrat> contrat = contratRepository.findById(id);
        return contrat.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/contrats/resident/{residentId}")
    public ResponseEntity<?> getContratByResident(@PathVariable Long residentId) {
        Optional<Contrat> contrat = contratRepository.findByResidentId(residentId);
        return contrat.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/contrats/{id}")
    public ResponseEntity<?> updateContrat(@PathVariable Long id, @RequestBody Contrat contratDetails) {
        Optional<Contrat> contratOpt = contratRepository.findById(id);
        if (contratOpt.isPresent()) {
            Contrat contrat = contratOpt.get();
            contrat.setDateDebut(contratDetails.getDateDebut());
            contrat.setDateFin(contratDetails.getDateFin());
            contrat.setTarifMensuel(contratDetails.getTarifMensuel());
            contrat.setConditions(contratDetails.getConditions());
            contrat.setTypeHebergement(contratDetails.getTypeHebergement());
            contrat.setDureeContrat(contratDetails.getDureeContrat());
            contrat.setConditionsParticulieres(contratDetails.getConditionsParticulieres());
            
            Contrat updatedContrat = contratRepository.save(contrat);
            return ResponseEntity.ok(updatedContrat);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/contrats/{id}/renouveler")
    public ResponseEntity<?> renouvelerContrat(@PathVariable Long id, @RequestBody Map<String, Object> data) {
        Optional<Contrat> contratOpt = contratRepository.findById(id);
        if (contratOpt.isPresent()) {
            Contrat contrat = contratOpt.get();
            LocalDate nouvelleDateFin = LocalDate.parse(data.get("nouvelleDateFin").toString());
            Double nouveauTarif = data.get("nouveauTarif") != null ? 
                Double.valueOf(data.get("nouveauTarif").toString()) : null;
            
            contrat.renouveler(nouvelleDateFin, nouveauTarif);
            contratRepository.save(contrat);
            return ResponseEntity.ok("Contrat renouvelé jusqu'au " + nouvelleDateFin);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/contrats/{id}/resilier")
    public ResponseEntity<?> resilierContrat(@PathVariable Long id, @RequestBody Map<String, String> data) {
        Optional<Contrat> contratOpt = contratRepository.findById(id);
        if (contratOpt.isPresent()) {
            Contrat contrat = contratOpt.get();
            contrat.resilier(data.get("motif"));
            contratRepository.save(contrat);
            return ResponseEntity.ok("Contrat résilié");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/contrats/actifs")
    public List<Contrat> getContratsActifs() {
        return contratRepository.findByActifTrue();
    }

    @GetMapping("/contrats/expires")
    public List<Contrat> getContratsExpires() {
        return contratRepository.findContratsExpires(LocalDate.now());
    }

    @GetMapping("/contrats/expirant-soon")
    public List<Contrat> getContratsExpirantSoon() {
        LocalDate in30Days = LocalDate.now().plusDays(30);
        return contratRepository.findContratsExpirantBetween(LocalDate.now(), in30Days);
    }

    @DeleteMapping("/contrats/{id}")
    public ResponseEntity<?> deleteContrat(@PathVariable Long id) {
        if (contratRepository.existsById(id)) {
            contratRepository.deleteById(id);
            return ResponseEntity.ok("Contrat supprimé avec succès");
        }
        return ResponseEntity.notFound().build();
    }
}
