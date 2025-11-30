/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.controller;

import com.example.centre.entity.Planning;
import com.example.centre.entity.TypeShift;
import com.example.centre.repository.PlanningRepository;
import com.example.centre.repository.PersonnelRepository;
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
public class PlanningController {

    @Autowired
    private PlanningRepository planningRepository;

    @Autowired
    private PersonnelRepository personnelRepository;

    @PostMapping("/plannings")
    public ResponseEntity<?> createPlanning(@RequestBody Planning planning) {
        try {
            // Vérifier si le personnel existe
            if (!personnelRepository.existsById(planning.getPersonnel().getId())) {
                return ResponseEntity.badRequest().body("Personnel non trouvé");
            }

            Planning savedPlanning = planningRepository.save(planning);
            return ResponseEntity.ok(savedPlanning);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création du planning: " + e.getMessage());
        }
    }

    @GetMapping("/plannings")
    public List<Planning> getAllPlannings() {
        return planningRepository.findAll();
    }

    @GetMapping("/plannings/{id}")
    public ResponseEntity<?> getPlanningById(@PathVariable Long id) {
        Optional<Planning> planning = planningRepository.findById(id);
        return planning.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/plannings/personnel/{personnelId}")
    public List<Planning> getPlanningsByPersonnel(@PathVariable Long personnelId) {
        return planningRepository.findByPersonnelId(personnelId);
    }

    @GetMapping("/plannings/date/{date}")
    public List<Planning> getPlanningsByDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        return planningRepository.findByDate(localDate);
    }

    @PutMapping("/plannings/{id}")
    public ResponseEntity<?> updatePlanning(@PathVariable Long id, @RequestBody Planning planningDetails) {
        Optional<Planning> planningOpt = planningRepository.findById(id);
        if (planningOpt.isPresent()) {
            Planning planning = planningOpt.get();
            planning.setDate(planningDetails.getDate());
            planning.setHeureDebut(planningDetails.getHeureDebut());
            planning.setHeureFin(planningDetails.getHeureFin());
            planning.setShift(planningDetails.getShift());
            planning.setActivite(planningDetails.getActivite());
            planning.setLieu(planningDetails.getLieu());
            planning.setNotes(planningDetails.getNotes());
            
            Planning updatedPlanning = planningRepository.save(planning);
            return ResponseEntity.ok(updatedPlanning);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/plannings/{id}")
    public ResponseEntity<?> deletePlanning(@PathVariable Long id) {
        if (planningRepository.existsById(id)) {
            planningRepository.deleteById(id);
            return ResponseEntity.ok("Planning supprimé avec succès");
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/plannings/{id}/valider")
    public ResponseEntity<?> validerPlanning(@PathVariable Long id) {
        Optional<Planning> planningOpt = planningRepository.findById(id);
        if (planningOpt.isPresent()) {
            Planning planning = planningOpt.get();
            planning.valider();
            planningRepository.save(planning);
            return ResponseEntity.ok("Planning validé");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/plannings/valides/date/{date}")
    public List<Planning> getPlanningsValidesDuJour(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        return planningRepository.findPlanningValideDuJour(localDate);
    }

    @GetMapping("/plannings/non-valides")
    public List<Planning> getPlanningsNonValides() {
        return planningRepository.findByValideFalse();
    }

    @GetMapping("/plannings/period")
    public List<Planning> getPlanningsByPeriod(@RequestParam String startDate, @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return planningRepository.findByDateBetween(start, end);
    }

    @GetMapping("/plannings/personnel/{personnelId}/period")
    public List<Planning> getPlanningsByPersonnelAndPeriod(@PathVariable Long personnelId, 
                                                          @RequestParam String startDate, 
                                                          @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return planningRepository.findByPersonnelAndDateBetween(personnelId, start, end);
    }
}
