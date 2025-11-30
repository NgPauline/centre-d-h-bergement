/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "administrations_medicaments")
public class AdministrationMedicament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;
    
    @Column(nullable = false)
    private LocalDateTime dateHeureAdministration;
    
    private LocalDateTime dateHeurePlanifiee;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "infirmier_id")
    private Personnel infirmier;
    
    private Boolean administre = false;
    private String observation;
    private String reaction;
    private Double doseAdministree;
    
    // Constructeurs
    public AdministrationMedicament() {}
    
    public AdministrationMedicament(Prescription prescription, LocalDateTime dateHeurePlanifiee) {
        this.prescription = prescription;
        this.dateHeurePlanifiee = dateHeurePlanifiee;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Prescription getPrescription() { return prescription; }
    public void setPrescription(Prescription prescription) { this.prescription = prescription; }
    
    public LocalDateTime getDateHeureAdministration() { return dateHeureAdministration; }
    public void setDateHeureAdministration(LocalDateTime dateHeureAdministration) { this.dateHeureAdministration = dateHeureAdministration; }
    
    public LocalDateTime getDateHeurePlanifiee() { return dateHeurePlanifiee; }
    public void setDateHeurePlanifiee(LocalDateTime dateHeurePlanifiee) { this.dateHeurePlanifiee = dateHeurePlanifiee; }
    
    public Personnel getInfirmier() { return infirmier; }
    public void setInfirmier(Personnel infirmier) { this.infirmier = infirmier; }
    
    public Boolean getAdministre() { return administre; }
    public void setAdministre(Boolean administre) { this.administre = administre; }
    
    public String getObservation() { return observation; }
    public void setObservation(String observation) { this.observation = observation; }
    
    public String getReaction() { return reaction; }
    public void setReaction(String reaction) { this.reaction = reaction; }
    
    public Double getDoseAdministree() { return doseAdministree; }
    public void setDoseAdministree(Double doseAdministree) { this.doseAdministree = doseAdministree; }
    
    // Méthodes métier
    public void marquerCommeAdministre(Personnel infirmier, String observation) {
        this.administre = true;
        this.infirmier = infirmier;
        this.dateHeureAdministration = LocalDateTime.now();
        this.observation = observation;
    }
    
    public void signalerOubli(String motif) {
        this.administre = false;
        this.observation = "OUBLI - " + motif;
        this.dateHeureAdministration = null;
    }
    
    public boolean estEnRetard() {
        return !administre && dateHeurePlanifiee != null && 
               LocalDateTime.now().isAfter(dateHeurePlanifiee);
    }
    
    public long getRetardEnMinutes() {
        if (administre || dateHeurePlanifiee == null) return 0;
        return java.time.temporal.ChronoUnit.MINUTES.between(dateHeurePlanifiee, LocalDateTime.now());
    }
    
    public boolean estPlanifieePourAujourdhui() {
        return dateHeurePlanifiee != null && 
               dateHeurePlanifiee.toLocalDate().equals(LocalDateTime.now().toLocalDate());
    }
}