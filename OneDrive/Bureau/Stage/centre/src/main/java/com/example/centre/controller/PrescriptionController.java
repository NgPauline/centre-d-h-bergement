/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.controller;

import com.example.centre.entity.Prescription;
import com.example.centre.repository.PrescriptionRepository;
import com.example.centre.repository.DossierMedicalRepository;
import com.example.centre.repository.MedicamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/infirmier")
@CrossOrigin(origins = "*")
public class PrescriptionController {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private DossierMedicalRepository dossierMedicalRepository;

    @Autowired
    private MedicamentRepository medicamentRepository;

    @PostMapping("/prescriptions")
    public ResponseEntity<?> createPrescription(@RequestBody Prescription prescription) {
        try {
            // Vérifications
            if (!dossierMedicalRepository.existsById(prescription.getDossierMedical().getId())) {
                return ResponseEntity.badRequest().body("Dossier médical non trouvé");
            }
            if (!medicamentRepository.existsById(prescription.getMedicament().getId())) {
                return ResponseEntity.badRequest().body("Médicament non trouvé");
            }

            Prescription savedPrescription = prescriptionRepository.save(prescription);
            return ResponseEntity.ok(savedPrescription);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création de la prescription: " + e.getMessage());
        }
    }

    @GetMapping("/prescriptions")
    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    @GetMapping("/prescriptions/{id}")
    public ResponseEntity<?> getPrescriptionById(@PathVariable Long id) {
        Optional<Prescription> prescription = prescriptionRepository.findById(id);
        return prescription.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/prescriptions/resident/{residentId}")
    public List<Prescription> getPrescriptionsByResident(@PathVariable Long residentId) {
        return prescriptionRepository.findByDossierMedicalResidentId(residentId);
    }

    @GetMapping("/prescriptions/actives")
    public List<Prescription> getPrescriptionsActives() {
        return prescriptionRepository.findPrescriptionsActives(LocalDate.now());
    }

    @PutMapping("/prescriptions/{id}")
    public ResponseEntity<?> updatePrescription(@PathVariable Long id, @RequestBody Prescription prescriptionDetails) {
        Optional<Prescription> prescriptionOpt = prescriptionRepository.findById(id);
        if (prescriptionOpt.isPresent()) {
            Prescription prescription = prescriptionOpt.get();
            prescription.setPosologie(prescriptionDetails.getPosologie());
            prescription.setDateDebut(prescriptionDetails.getDateDebut());
            prescription.setDateFin(prescriptionDetails.getDateFin());
            prescription.setInstructions(prescriptionDetails.getInstructions());
            prescription.setActive(prescriptionDetails.getActive());
            prescription.setMotif(prescriptionDetails.getMotif());
            
            Prescription updatedPrescription = prescriptionRepository.save(prescription);
            return ResponseEntity.ok(updatedPrescription);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/prescriptions/{id}")
    public ResponseEntity<?> deletePrescription(@PathVariable Long id) {
        if (prescriptionRepository.existsById(id)) {
            prescriptionRepository.deleteById(id);
            return ResponseEntity.ok("Prescription supprimée avec succès");
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/prescriptions/{id}/renouveler")
    public ResponseEntity<?> renouvelerPrescription(@PathVariable Long id, @RequestBody Map<String, String> data) {
        Optional<Prescription> prescriptionOpt = prescriptionRepository.findById(id);
        if (prescriptionOpt.isPresent()) {
            Prescription prescription = prescriptionOpt.get();
            LocalDate nouvelleDateFin = LocalDate.parse(data.get("nouvelleDateFin"));
            prescription.renouveler(nouvelleDateFin);
            prescriptionRepository.save(prescription);
            return ResponseEntity.ok("Prescription renouvelée jusqu'au " + nouvelleDateFin);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/prescriptions/{id}/suspendre")
    public ResponseEntity<?> suspendrePrescription(@PathVariable Long id) {
        Optional<Prescription> prescriptionOpt = prescriptionRepository.findById(id);
        if (prescriptionOpt.isPresent()) {
            Prescription prescription = prescriptionOpt.get();
            prescription.suspendre();
            prescriptionRepository.save(prescription);
            return ResponseEntity.ok("Prescription suspendue");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/prescriptions/medecin/{medecin}")
    public List<Prescription> getPrescriptionsByMedecin(@PathVariable String medecin) {
        return prescriptionRepository.findByMedecinContaining(medecin);
    }

    @GetMapping("/prescriptions/expirees")
    public List<Prescription> getPrescriptionsExpirees() {
        return prescriptionRepository.findPrescriptionsExpirees(LocalDate.now());
    }
}