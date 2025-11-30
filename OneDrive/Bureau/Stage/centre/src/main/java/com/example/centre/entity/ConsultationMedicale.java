/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultations_medicales")
public class ConsultationMedicale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_id", nullable = false)
    private Resident resident;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dossier_medical_id")
    private DossierMedical dossierMedical;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personnel_id")
    private Personnel personnel;
    
    private String medecinExterne;
    private String specialite;
    private String lieuConsultation;
    
    @Column(nullable = false)
    private LocalDateTime dateHeure;
    
    private String motif;
    private String diagnostic;
    private String traitementPrescrit;
    private String examensDemandes;
    
    @Enumerated(EnumType.STRING)
    private TypeConsultation type;
    
    private Boolean urgence = false;
    private String observations;
    private String compteRendu;
    private LocalDateTime dateProchainRendezVous;
    
    // Constructeurs
    public ConsultationMedicale() {}
    
    public ConsultationMedicale(Resident resident, LocalDateTime dateHeure, String motif) {
        this.resident = resident;
        this.dateHeure = dateHeure;
        this.motif = motif;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Resident getResident() { return resident; }
    public void setResident(Resident resident) { this.resident = resident; }
    
    public Personnel getPersonnel() { return personnel; }
    public void setPersonnel(Personnel personnel) { this.personnel = personnel; }
    
    public String getMedecinExterne() { return medecinExterne; }
    public void setMedecinExterne(String medecinExterne) { this.medecinExterne = medecinExterne; }
    
    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }
    
    public String getLieuConsultation() { return lieuConsultation; }
    public void setLieuConsultation(String lieuConsultation) { this.lieuConsultation = lieuConsultation; }
    
    public LocalDateTime getDateHeure() { return dateHeure; }
    public void setDateHeure(LocalDateTime dateHeure) { this.dateHeure = dateHeure; }
    
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
    
    public String getDiagnostic() { return diagnostic; }
    public void setDiagnostic(String diagnostic) { this.diagnostic = diagnostic; }
    
    public String getTraitementPrescrit() { return traitementPrescrit; }
    public void setTraitementPrescrit(String traitementPrescrit) { this.traitementPrescrit = traitementPrescrit; }
    
    public String getExamensDemandes() { return examensDemandes; }
    public void setExamensDemandes(String examensDemandes) { this.examensDemandes = examensDemandes; }
    
    public TypeConsultation getType() { return type; }
    public void setType(TypeConsultation type) { this.type = type; }
    
    public Boolean getUrgence() { return urgence; }
    public void setUrgence(Boolean urgence) { this.urgence = urgence; }
    
    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
    
    public String getCompteRendu() { return compteRendu; }
    public void setCompteRendu(String compteRendu) { this.compteRendu = compteRendu; }
    
    public LocalDateTime getDateProchainRendezVous() { return dateProchainRendezVous; }
    public void setDateProchainRendezVous(LocalDateTime dateProchainRendezVous) { this.dateProchainRendezVous = dateProchainRendezVous; }

    // Méthodes métier
    public String genererCompteRendu() {
        StringBuilder cr = new StringBuilder();
        cr.append("Consultation du ").append(dateHeure.toLocalDate()).append("\n");
        cr.append("Patient: ").append(resident.getNomComplet()).append("\n");
        cr.append("Motif: ").append(motif).append("\n");
        if (diagnostic != null) cr.append("Diagnostic: ").append(diagnostic).append("\n");
        if (traitementPrescrit != null) cr.append("Traitement: ").append(traitementPrescrit).append("\n");
        if (observations != null) cr.append("Observations: ").append(observations).append("\n");
        return cr.toString();
    }
    
    public boolean estPassee() {
        return LocalDateTime.now().isAfter(dateHeure);
    }
    
    public boolean estProgrammee() {
        return LocalDateTime.now().isBefore(dateHeure);
    }
    
    public long getJoursAvantConsultation() {
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDateTime.now().toLocalDate(), dateHeure.toLocalDate());
    }
    
    public boolean necessiteSuivi() {
        return dateProchainRendezVous != null;
    }

}