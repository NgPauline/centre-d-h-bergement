/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prescriptions")
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dossier_medical_id")
    private DossierMedical dossierMedical;
    
    private String medecinExterne;
    private String numeroInami;
    private String specialite;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicament_id")
    private Medicament medicament;
    
    // CORRECTION : Ajouter les getters et setters manquants pour resident
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_id", nullable = false)
    private Resident resident;
    
    private String posologie;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String instructions;
    private Boolean active = true;
    private String motif;
    
    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AdministrationMedicament> administrations = new ArrayList<>();
    
    // Constructeurs
    public Prescription() {}
    
    // CORRECTION : Ajouter resident dans le constructeur
    public Prescription(Resident resident, Medicament medicament, String posologie, LocalDate dateDebut, LocalDate dateFin) {
        this.resident = resident;
        this.medicament = medicament;
        this.posologie = posologie;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public DossierMedical getDossierMedical() { return dossierMedical; }
    public void setDossierMedical(DossierMedical dossierMedical) { this.dossierMedical = dossierMedical; }
    
    public String getMedecinExterne() { return medecinExterne; }
    public void setMedecinExterne(String medecinExterne) { this.medecinExterne = medecinExterne; }
    
    public String getNumeroInami() { return numeroInami; }
    public void setNumeroInami(String numeroInami) { this.numeroInami = numeroInami; }
    
    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }
    
    public Medicament getMedicament() { return medicament; }
    public void setMedicament(Medicament medicament) { this.medicament = medicament; }
    
    // CORRECTION : Ajouter les getters et setters pour resident
    public Resident getResident() { return resident; }
    public void setResident(Resident resident) { this.resident = resident; }
    
    public String getPosologie() { return posologie; }
    public void setPosologie(String posologie) { this.posologie = posologie; }
    
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
    
    public List<AdministrationMedicament> getAdministrations() { return administrations; }
    public void setAdministrations(List<AdministrationMedicament> administrations) { this.administrations = administrations; }

    // Méthodes métier
    public boolean estActive() {
        return active && 
               dateDebut != null && 
               LocalDate.now().isAfter(dateDebut) && 
               (dateFin == null || LocalDate.now().isBefore(dateFin) || LocalDate.now().isEqual(dateFin));
    }
    
    public boolean estExpiree() {
        return dateFin != null && LocalDate.now().isAfter(dateFin);
    }
    
    public void renouveler(LocalDate nouvelleDateFin) {
        this.dateFin = nouvelleDateFin;
        this.active = true;
    }
    
    public void suspendre() {
        this.active = false;
    }
    
    public long getJoursRestants() {
        if (dateFin == null) return Long.MAX_VALUE;
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), dateFin);
    }
    
    // CORRECTION : Ajouter une méthode qui utilise resident
    public String getInfoComplete() {
        return "Prescription pour " + resident.getNomComplet() + " - " + medicament.getNom() + " - " + posologie;
    }
}