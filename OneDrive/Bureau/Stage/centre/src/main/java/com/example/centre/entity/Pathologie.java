/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pathologies")
public class Pathologie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    private String description;
    private LocalDate dateDiagnostic;
    private Boolean chronique = false;
    private String traitement;
    private String symptomes;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dossier_medical_id")
    private DossierMedical dossierMedical;
    
    // Constructeurs
    public Pathologie() {}
    
    public Pathologie(String nom, LocalDate dateDiagnostic) {
        this.nom = nom;
        this.dateDiagnostic = dateDiagnostic;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public LocalDate getDateDiagnostic() { return dateDiagnostic; }
    public void setDateDiagnostic(LocalDate dateDiagnostic) { this.dateDiagnostic = dateDiagnostic; }
    
    public Boolean getChronique() { return chronique; }
    public void setChronique(Boolean chronique) { this.chronique = chronique; }
    
    public String getTraitement() { return traitement; }
    public void setTraitement(String traitement) { this.traitement = traitement; }
    
    public String getSymptomes() { return symptomes; }
    public void setSymptomes(String symptomes) { this.symptomes = symptomes; }
    
    public DossierMedical getDossierMedical() { return dossierMedical; }
    public void setDossierMedical(DossierMedical dossierMedical) { this.dossierMedical = dossierMedical; }
    
    
    // Méthodes métier
    public boolean estChronique() {
        return chronique;
    }
    
    public long getDureeDepuisDiagnostic() {
        if (dateDiagnostic == null) return 0;
        return java.time.temporal.ChronoUnit.DAYS.between(dateDiagnostic, LocalDate.now());
    }
    
}