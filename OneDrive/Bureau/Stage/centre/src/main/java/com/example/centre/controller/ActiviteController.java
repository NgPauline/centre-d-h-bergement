/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.controller;

import com.example.centre.entity.Activite;
import com.example.centre.entity.TypeActivite;
import com.example.centre.repository.ActiviteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/educateur")
@CrossOrigin(origins = "*")
public class ActiviteController {

    @Autowired
    private ActiviteRepository activiteRepository;

    @PostMapping("/activites")
    public ResponseEntity<?> createActivite(@RequestBody Activite activite) {
        try {
            Activite savedActivite = activiteRepository.save(activite);
            return ResponseEntity.ok(savedActivite);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création de l'activité: " + e.getMessage());
        }
    }

    @GetMapping("/activites")
    public List<Activite> getAllActivites() {
        return activiteRepository.findAll();
    }

    @GetMapping("/activites/{id}")
    public ResponseEntity<?> getActiviteById(@PathVariable Long id) {
        Optional<Activite> activite = activiteRepository.findById(id);
        return activite.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/activites/{id}")
    public ResponseEntity<?> updateActivite(@PathVariable Long id, @RequestBody Activite activiteDetails) {
        Optional<Activite> activiteOpt = activiteRepository.findById(id);
        if (activiteOpt.isPresent()) {
            Activite activite = activiteOpt.get();
            activite.setNom(activiteDetails.getNom());
            activite.setDescription(activiteDetails.getDescription());
            activite.setType(activiteDetails.getType());
            activite.setDateHeure(activiteDetails.getDateHeure());
            activite.setDuree(activiteDetails.getDuree());
            activite.setCapaciteMax(activiteDetails.getCapaciteMax());
            activite.setLieu(activiteDetails.getLieu());
            activite.setMaterielNecessaire(activiteDetails.getMaterielNecessaire());
            activite.setObjectifs(activiteDetails.getObjectifs());
            activite.setResponsable(activiteDetails.getResponsable());
            
            Activite updatedActivite = activiteRepository.save(activite);
            return ResponseEntity.ok(updatedActivite);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/activites/{id}")
    public ResponseEntity<?> deleteActivite(@PathVariable Long id) {
        if (activiteRepository.existsById(id)) {
            activiteRepository.deleteById(id);
            return ResponseEntity.ok("Activité supprimée avec succès");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/activites/type/{type}")
    public List<Activite> getActivitesByType(@PathVariable TypeActivite type) {
        return activiteRepository.findByType(type);
    }

    @GetMapping("/activites/futures")
    public List<Activite> getActivitesFutures() {
        return activiteRepository.findFutureActivities(LocalDateTime.now());
    }

    @GetMapping("/activites/passees")
    public List<Activite> getActivitesPassees() {
        return activiteRepository.findPastActivities(LocalDateTime.now());
    }

    @GetMapping("/activites/disponibles")
    public List<Activite> getActivitesAvecPlaces() {
        return activiteRepository.findActivitiesWithAvailablePlaces();
    }

    @GetMapping("/activites/responsable/{responsableId}")
    public List<Activite> getActivitesParResponsable(@PathVariable Long responsableId) {
        return activiteRepository.findByResponsableId(responsableId);
    }
}
