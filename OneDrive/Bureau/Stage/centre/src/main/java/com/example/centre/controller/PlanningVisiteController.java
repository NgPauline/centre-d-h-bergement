/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.controller;

import com.example.centre.entity.PlanningVisite;
import com.example.centre.entity.TypeVisite;
import com.example.centre.repository.PlanningVisiteRepository;
import com.example.centre.repository.ResidentRepository;
import com.example.centre.repository.PersonnelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PlanningVisiteController {

    @Autowired
    private PlanningVisiteRepository planningVisiteRepository;

    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private PersonnelRepository personnelRepository;

    @PostMapping("/infirmier/planning-visites")
    public ResponseEntity<?> createPlanningVisite(@RequestBody PlanningVisite planningVisite) {
        try {
            // Vérifications
            if (!residentRepository.existsById(planningVisite.getResident().getId())) {
                return ResponseEntity.badRequest().body("Résident non trouvé");
            }
            if (!personnelRepository.existsById(planningVisite.getSoignant().getId())) {
                return ResponseEntity.badRequest().body("Soignant non trouvé");
            }

            PlanningVisite savedPlanning = planningVisiteRepository.save(planningVisite);
            return ResponseEntity.ok(savedPlanning);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création du planning de visite: " + e.getMessage());
        }
    }

    @GetMapping("/planning-visites")
    public List<PlanningVisite> getAllPlanningVisites() {
        return planningVisiteRepository.findAll();
    }

    @GetMapping("/planning-visites/{id}")
    public ResponseEntity<?> getPlanningVisiteById(@PathVariable Long id) {
        Optional<PlanningVisite> planning = planningVisiteRepository.findById(id);
        return planning.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/planning-visites/resident/{residentId}")
    public List<PlanningVisite> getPlanningByResident(@PathVariable Long residentId) {
        return planningVisiteRepository.findByResidentId(residentId);
    }

    @GetMapping("/planning-visites/soignant/{soignantId}")
    public List<PlanningVisite> getPlanningBySoignant(@PathVariable Long soignantId) {
        return planningVisiteRepository.findBySoignantId(soignantId);
    }

    @PutMapping("/infirmier/planning-visites/{id}/effectuer")
    public ResponseEntity<?> marquerVisiteEffectuee(@PathVariable Long id, @RequestBody Map<String, String> data) {
        Optional<PlanningVisite> planningOpt = planningVisiteRepository.findById(id);
        if (planningOpt.isPresent()) {
            PlanningVisite planning = planningOpt.get();
            planning.marquerEffectuee(data.get("soinsRealises"), data.get("observations"));
            planningVisiteRepository.save(planning);
            return ResponseEntity.ok("Visite marquée comme effectuée");
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/infirmier/planning-visites/{id}/reprogrammer")
    public ResponseEntity<?> reprogrammerVisite(@PathVariable Long id, @RequestBody Map<String, String> data) {
        Optional<PlanningVisite> planningOpt = planningVisiteRepository.findById(id);
        if (planningOpt.isPresent()) {
            PlanningVisite planning = planningOpt.get();
            // Implémentation de la reprogrammation
            planning.setObservations("Reprogrammée - " + data.get("motif"));
            planningVisiteRepository.save(planning);
            return ResponseEntity.ok("Visite reprogrammée");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/planning-visites/en-retard")
    public List<PlanningVisite> getVisitesEnRetard() {
        return planningVisiteRepository.findVisitesEnRetard(LocalDate.now());
    }

    @GetMapping("/infirmier/planning-visites/infirmier-planifiees")
    public List<PlanningVisite> getVisitesInfirmierPlanifiees() {
        return planningVisiteRepository.findVisitesInfirmierPlanifiees();
    }

    @GetMapping("/aide-soignant/planning-visites/aide-soignant-planifiees")
    public List<PlanningVisite> getVisitesAideSoignantPlanifiees() {
        return planningVisiteRepository.findVisitesAideSoignantPlanifiees();
    }

    @DeleteMapping("/infirmier/planning-visites/{id}")
    public ResponseEntity<?> deletePlanningVisite(@PathVariable Long id) {
        if (planningVisiteRepository.existsById(id)) {
            planningVisiteRepository.deleteById(id);
            return ResponseEntity.ok("Planning de visite supprimé avec succès");
        }
        return ResponseEntity.notFound().build();
    }
}
