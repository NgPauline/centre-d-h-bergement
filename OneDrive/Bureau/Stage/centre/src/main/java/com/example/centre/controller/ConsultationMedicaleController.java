/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.controller;

import com.example.centre.entity.ConsultationMedicale;
import com.example.centre.entity.TypeConsultation;
import com.example.centre.repository.ConsultationMedicaleRepository;
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
public class ConsultationMedicaleController {

    @Autowired
    private ConsultationMedicaleRepository consultationRepository;

    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private PersonnelRepository personnelRepository;

    @PostMapping("/consultations")
    public ResponseEntity<?> createConsultation(@RequestBody ConsultationMedicale consultation) {
        try {
            // Vérifier si le résident existe
            if (!residentRepository.existsById(consultation.getResident().getId())) {
                return ResponseEntity.badRequest().body("Résident non trouvé");
            }

            ConsultationMedicale savedConsultation = consultationRepository.save(consultation);
            return ResponseEntity.ok(savedConsultation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création de la consultation: " + e.getMessage());
        }
    }

    @GetMapping("/consultations")
    public List<ConsultationMedicale> getAllConsultations() {
        return consultationRepository.findAll();
    }

    @GetMapping("/consultations/{id}")
    public ResponseEntity<?> getConsultationById(@PathVariable Long id) {
        Optional<ConsultationMedicale> consultation = consultationRepository.findById(id);
        return consultation.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/consultations/resident/{residentId}")
    public List<ConsultationMedicale> getConsultationsByResident(@PathVariable Long residentId) {
        return consultationRepository.findByResidentId(residentId);
    }

    @GetMapping("/consultations/futures")
    public List<ConsultationMedicale> getConsultationsFutures() {
        return consultationRepository.findFutureConsultations(LocalDateTime.now());
    }

    @PutMapping("/consultations/{id}")
    public ResponseEntity<?> updateConsultation(@PathVariable Long id, @RequestBody ConsultationMedicale consultationDetails) {
        Optional<ConsultationMedicale> consultationOpt = consultationRepository.findById(id);
        if (consultationOpt.isPresent()) {
            ConsultationMedicale consultation = consultationOpt.get();
            consultation.setDateHeure(consultationDetails.getDateHeure());
            consultation.setMotif(consultationDetails.getMotif());
            consultation.setDiagnostic(consultationDetails.getDiagnostic());
            consultation.setTraitementPrescrit(consultationDetails.getTraitementPrescrit());
            consultation.setObservations(consultationDetails.getObservations());
            consultation.setType(consultationDetails.getType());
            consultation.setUrgence(consultationDetails.getUrgence());
            consultation.setCompteRendu(consultationDetails.getCompteRendu());
            
            ConsultationMedicale updatedConsultation = consultationRepository.save(consultation);
            return ResponseEntity.ok(updatedConsultation);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/consultations/{id}")
    public ResponseEntity<?> deleteConsultation(@PathVariable Long id) {
        if (consultationRepository.existsById(id)) {
            consultationRepository.deleteById(id);
            return ResponseEntity.ok("Consultation supprimée avec succès");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/consultations/type/{type}")
    public List<ConsultationMedicale> getConsultationsByType(@PathVariable TypeConsultation type) {
        return consultationRepository.findByType(type);
    }

    @GetMapping("/consultations/urgence")
    public List<ConsultationMedicale> getConsultationsUrgence() {
        return consultationRepository.findByUrgenceTrue();
    }

    @GetMapping("/consultations/medecin/{medecin}")
    public List<ConsultationMedicale> getConsultationsByMedecin(@PathVariable String medecin) {
        return consultationRepository.findByMedecinContaining(medecin);
    }

    @GetMapping("/consultations/specialite/{specialite}")
    public List<ConsultationMedicale> getConsultationsBySpecialite(@PathVariable String specialite) {
        return consultationRepository.findBySpecialite(specialite);
    }

    @PutMapping("/consultations/{id}/generer-compte-rendu")
    public ResponseEntity<?> genererCompteRendu(@PathVariable Long id) {
        Optional<ConsultationMedicale> consultationOpt = consultationRepository.findById(id);
        if (consultationOpt.isPresent()) {
            ConsultationMedicale consultation = consultationOpt.get();
            String compteRendu = consultation.genererCompteRendu();
            consultation.setCompteRendu(compteRendu);
            consultationRepository.save(consultation);
            return ResponseEntity.ok(Map.of("compteRendu", compteRendu));
        }
        return ResponseEntity.notFound().build();
    }
}