/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.controller;

import com.example.centre.entity.AppelUrgence;
import com.example.centre.entity.TypeUrgence;
import com.example.centre.repository.AppelUrgenceRepository;
import com.example.centre.repository.ResidentRepository;
import com.example.centre.repository.PersonnelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/infirmier")
@CrossOrigin(origins = "*")
public class AppelUrgenceController {

    @Autowired
    private AppelUrgenceRepository appelUrgenceRepository;

    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private PersonnelRepository personnelRepository;

    @PostMapping("/appels-urgence")
    public ResponseEntity<?> createAppelUrgence(@RequestBody Map<String, Object> appelData) {
        try {
            Long residentId = Long.valueOf(appelData.get("residentId").toString());
            Long appelantId = Long.valueOf(appelData.get("appelantId").toString());
            TypeUrgence type = TypeUrgence.valueOf(appelData.get("type").toString());

            // Vérifications
            if (!residentRepository.existsById(residentId)) {
                return ResponseEntity.badRequest().body("Résident non trouvé");
            }
            if (!personnelRepository.existsById(appelantId)) {
                return ResponseEntity.badRequest().body("Personnel non trouvé");
            }

            AppelUrgence appel = new AppelUrgence(
                residentRepository.findById(residentId).get(),
                personnelRepository.findById(appelantId).get(),
                type
            );

            appel.setDescription((String) appelData.get("description"));
            appel.setMesuresPrises((String) appelData.get("mesuresPrises"));

            AppelUrgence savedAppel = appelUrgenceRepository.save(appel);
            return ResponseEntity.ok(savedAppel);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création de l'appel d'urgence: " + e.getMessage());
        }
    }

    @GetMapping("/appels-urgence")
    public List<AppelUrgence> getAllAppelsUrgence() {
        return appelUrgenceRepository.findAll();
    }

    @GetMapping("/appels-urgence/{id}")
    public ResponseEntity<?> getAppelUrgenceById(@PathVariable Long id) {
        Optional<AppelUrgence> appel = appelUrgenceRepository.findById(id);
        return appel.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/appels-urgence/resident/{residentId}")
    public List<AppelUrgence> getAppelsByResident(@PathVariable Long residentId) {
        return appelUrgenceRepository.findByResidentId(residentId);
    }

    @PutMapping("/appels-urgence/{id}/appeler-ambulance")
    public ResponseEntity<?> appelerAmbulance(@PathVariable Long id, @RequestBody Map<String, String> data) {
        Optional<AppelUrgence> appelOpt = appelUrgenceRepository.findById(id);
        if (appelOpt.isPresent()) {
            AppelUrgence appel = appelOpt.get();
            appel.appelerAmbulance(data.get("numero"), data.get("destination"));
            appelUrgenceRepository.save(appel);
            return ResponseEntity.ok("Ambulance appelée");
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/appels-urgence/{id}/notifier-famille")
    public ResponseEntity<?> notifierFamille(@PathVariable Long id, @RequestBody Map<String, String> data) {
        Optional<AppelUrgence> appelOpt = appelUrgenceRepository.findById(id);
        if (appelOpt.isPresent()) {
            AppelUrgence appel = appelOpt.get();
            // CORRECTION : Utiliser notifierFamille() au lieu de informerFamille()
            appel.notifierFamille(data.get("contact"), data.get("message"));
            appelUrgenceRepository.save(appel);
            return ResponseEntity.ok("Famille notifiée");
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/appels-urgence/{id}/cloturer")
    public ResponseEntity<?> cloturerAppel(@PathVariable Long id, @RequestBody Map<String, String> data) {
        Optional<AppelUrgence> appelOpt = appelUrgenceRepository.findById(id);
        if (appelOpt.isPresent()) {
            AppelUrgence appel = appelOpt.get();
            // CORRECTION : Ajouter la méthode cloturerIncident() dans l'entité ou utiliser une alternative
            appel.setSuivi(data.get("suitesDonnees"));
            // Marquer comme résolu si nécessaire
            if (data.get("suitesDonnees") != null && !data.get("suitesDonnees").isEmpty()) {
                appel.setSuivi("Clôturé - " + data.get("suitesDonnees"));
            }
            appelUrgenceRepository.save(appel);
            return ResponseEntity.ok("Appel d'urgence clôturé");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/appels-urgence/non-clotures")
    public List<AppelUrgence> getAppelsNonClotures() {
        return appelUrgenceRepository.findAppelsNonClotures();
    }

    @GetMapping("/appels-urgence/famille-non-notifiee")
    public List<AppelUrgence> getAppelsFamilleNonNotifiee() {
        return appelUrgenceRepository.findAppelsFamilleNonNotifiee();
    }
}