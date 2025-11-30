/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.controller;

import com.example.centre.entity.AdministrationMedicament;
import com.example.centre.repository.AdministrationMedicamentRepository;
import com.example.centre.repository.PrescriptionRepository;
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
public class AdministrationMedicamentController {

    @Autowired
    private AdministrationMedicamentRepository administrationRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PersonnelRepository personnelRepository;

    @PostMapping("/administrations-medicaments")
    public ResponseEntity<?> createAdministration(@RequestBody Map<String, Object> administrationData) {
        try {
            Long prescriptionId = Long.valueOf(administrationData.get("prescriptionId").toString());
            
            // Vérifications
            if (!prescriptionRepository.existsById(prescriptionId)) {
                return ResponseEntity.badRequest().body("Prescription non trouvée");
            }

            AdministrationMedicament administration = new AdministrationMedicament(
                prescriptionRepository.findById(prescriptionId).get(),
                LocalDateTime.parse(administrationData.get("dateHeurePlanifiee").toString())
            );

            AdministrationMedicament savedAdministration = administrationRepository.save(administration);
            return ResponseEntity.ok(savedAdministration);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création de l'administration: " + e.getMessage());
        }
    }

    @GetMapping("/administrations-medicaments")
    public List<AdministrationMedicament> getAllAdministrations() {
        return administrationRepository.findAll();
    }

    @GetMapping("/administrations-medicaments/{id}")
    public ResponseEntity<?> getAdministrationById(@PathVariable Long id) {
        Optional<AdministrationMedicament> administration = administrationRepository.findById(id);
        return administration.map(ResponseEntity::ok)
                           .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/administrations-medicaments/{id}/administrer")
    public ResponseEntity<?> marquerCommeAdministre(@PathVariable Long id, @RequestBody Map<String, Object> data) {
        Optional<AdministrationMedicament> administrationOpt = administrationRepository.findById(id);
        Optional<com.example.centre.entity.Personnel> infirmierOpt = personnelRepository.findById(
            Long.valueOf(data.get("infirmierId").toString())
        );

        if (administrationOpt.isPresent() && infirmierOpt.isPresent()) {
            AdministrationMedicament administration = administrationOpt.get();
            administration.marquerCommeAdministre(infirmierOpt.get(), (String) data.get("observation"));
            administrationRepository.save(administration);
            return ResponseEntity.ok("Médicament administré");
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/administrations-medicaments/{id}/signaler-oubli")
    public ResponseEntity<?> signalerOubli(@PathVariable Long id, @RequestBody Map<String, String> data) {
        Optional<AdministrationMedicament> administrationOpt = administrationRepository.findById(id);
        if (administrationOpt.isPresent()) {
            AdministrationMedicament administration = administrationOpt.get();
            administration.signalerOubli(data.get("motif"));
            administrationRepository.save(administration);
            return ResponseEntity.ok("Oubli signalé");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/administrations-medicaments/en-retard")
    public List<AdministrationMedicament> getAdministrationsEnRetard() {
        return administrationRepository.findAdministrationsEnRetard(LocalDateTime.now());
    }

    @GetMapping("/administrations-medicaments/du-jour")
    public List<AdministrationMedicament> getAdministrationsDuJour() {
        return administrationRepository.findAdministrationsDuJour();
    }

    @GetMapping("/administrations-medicaments/resident/{residentId}/en-attente")
    public List<AdministrationMedicament> getAdministrationsEnAttenteParResident(@PathVariable Long residentId) {
        return administrationRepository.findAdministrationsEnAttenteParResident(residentId);
    }

    @GetMapping("/administrations-medicaments/prescription/{prescriptionId}")
    public List<AdministrationMedicament> getAdministrationsByPrescription(@PathVariable Long prescriptionId) {
        return administrationRepository.findByPrescriptionId(prescriptionId);
    }

    @DeleteMapping("/administrations-medicaments/{id}")
    public ResponseEntity<?> deleteAdministration(@PathVariable Long id) {
        if (administrationRepository.existsById(id)) {
            administrationRepository.deleteById(id);
            return ResponseEntity.ok("Administration supprimée avec succès");
        }
        return ResponseEntity.notFound().build();
    }
}