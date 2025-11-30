/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dossiers_medicaux")
public class DossierMedical {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_id", nullable = false)
    private Resident resident;
    
    private String groupeSanguin;
    private Double poids;
    private Double taille;
    private String antecedents;
    private String traitementsEnCours;
    
    @OneToMany(mappedBy = "dossierMedical", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Allergie> allergies = new ArrayList<>();
    
    @OneToMany(mappedBy = "dossierMedical", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pathologie> pathologies = new ArrayList<>();
    
    @OneToMany(mappedBy = "dossierMedical", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Prescription> prescriptions = new ArrayList<>();
    
    @OneToMany(mappedBy = "dossierMedical", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ConsultationMedicale> consultations = new ArrayList<>();
    
    private LocalDateTime derniereMAJ = LocalDateTime.now();
    
    // Constructeurs
    public DossierMedical() {}
    
    public DossierMedical(Resident resident) {
        this.resident = resident;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Resident getResident() { return resident; }
    public void setResident(Resident resident) { this.resident = resident; }
    
    public String getGroupeSanguin() { return groupeSanguin; }
    public void setGroupeSanguin(String groupeSanguin) { this.groupeSanguin = groupeSanguin; }
    
    public Double getPoids() { return poids; }
    public void setPoids(Double poids) { this.poids = poids; }
    
    public Double getTaille() { return taille; }
    public void setTaille(Double taille) { this.taille = taille; }
    
    public String getAntecedents() { return antecedents; }
    public void setAntecedents(String antecedents) { this.antecedents = antecedents; }
    
    public String getTraitementsEnCours() { return traitementsEnCours; }
    public void setTraitementsEnCours(String traitementsEnCours) { this.traitementsEnCours = traitementsEnCours; }
    
    public List<Allergie> getAllergies() { return allergies; }
    public void setAllergies(List<Allergie> allergies) { this.allergies = allergies; }
    
    public List<Pathologie> getPathologies() { return pathologies; }
    public void setPathologies(List<Pathologie> pathologies) { this.pathologies = pathologies; }
    
    public List<Prescription> getPrescriptions() { return prescriptions; }
    public void setPrescriptions(List<Prescription> prescriptions) { this.prescriptions = prescriptions; }
    
    public List<ConsultationMedicale> getConsultations() { return consultations; }
    public void setConsultations(List<ConsultationMedicale> consultations) { this.consultations = consultations; }
    
    public LocalDateTime getDerniereMAJ() { return derniereMAJ; }
    public void setDerniereMAJ(LocalDateTime derniereMAJ) { this.derniereMAJ = derniereMAJ; }
    
    // Méthodes métier
    public void ajouterAllergie(Allergie allergie) {
        allergies.add(allergie);
        allergie.setDossierMedical(this);
        mettreAJour();
    }
    
    public void ajouterPathologie(Pathologie pathologie) {
        pathologies.add(pathologie);
        pathologie.setDossierMedical(this);
        mettreAJour();
    }
    
    public void ajouterPrescription(Prescription prescription) {
        prescriptions.add(prescription);
        prescription.setDossierMedical(this);
        mettreAJour();
    }
    
    public List<Prescription> getPrescriptionsActives() {
        return prescriptions.stream()
                .filter(Prescription::estActive)
                .toList();
    }
    
    public List<Allergie> getAllergiesCritiques() {
        return allergies.stream()
                .filter(Allergie::estCritique)
                .toList();
    }
    
    public void mettreAJour() {
        this.derniereMAJ = LocalDateTime.now();
    }
    
    public Double getIMC() {
        if (poids == null || taille == null || taille == 0) return null;
        return poids / ((taille / 100) * (taille / 100));
    }
    
}