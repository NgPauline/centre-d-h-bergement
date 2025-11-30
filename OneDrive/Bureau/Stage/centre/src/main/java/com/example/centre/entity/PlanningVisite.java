/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "planning_visites")
public class PlanningVisite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_id", nullable = false)
    private Resident resident;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "soignant_id", nullable = false)
    private Personnel soignant;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeVisite type;
    
    @Column(nullable = false)
    private LocalDate date;
    
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private Boolean effectuee = false;
    private String observations;
    private String soinsRealises;
    private String signatureSoignant;
    private LocalDate dateRealisation;
    
    // Constructeurs
    public PlanningVisite() {}
    
    public PlanningVisite(Resident resident, Personnel soignant, TypeVisite type, LocalDate date) {
        this.resident = resident;
        this.soignant = soignant;
        this.type = type;
        this.date = date;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Resident getResident() { return resident; }
    public void setResident(Resident resident) { this.resident = resident; }
    
    public Personnel getSoignant() { return soignant; }
    public void setSoignant(Personnel soignant) { this.soignant = soignant; }
    
    public TypeVisite getType() { return type; }
    public void setType(TypeVisite type) { this.type = type; }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public LocalTime getHeureDebut() { return heureDebut; }
    public void setHeureDebut(LocalTime heureDebut) { this.heureDebut = heureDebut; }
    
    public LocalTime getHeureFin() { return heureFin; }
    public void setHeureFin(LocalTime heureFin) { this.heureFin = heureFin; }
    
    public Boolean getEffectuee() { return effectuee; }
    public void setEffectuee(Boolean effectuee) { this.effectuee = effectuee; }
    
    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
    
    public String getSoinsRealises() { return soinsRealises; }
    public void setSoinsRealises(String soinsRealises) { this.soinsRealises = soinsRealises; }
    
    public String getSignatureSoignant() { return signatureSoignant; }
    public void setSignatureSoignant(String signatureSoignant) { this.signatureSoignant = signatureSoignant; }
    
    public LocalDate getDateRealisation() { return dateRealisation; }
    public void setDateRealisation(LocalDate dateRealisation) { this.dateRealisation = dateRealisation; }
    
    
    // Méthodes métier
    public void marquerEffectuee(String soinsRealises, String observations) {
        this.effectuee = true;
        this.soinsRealises = soinsRealises;
        this.observations = observations;
        this.dateRealisation = LocalDate.now();
    }
    
    public void reprogrammer(LocalDate nouvelleDate, LocalTime nouvelleHeure) {
        this.date = nouvelleDate;
        this.heureDebut = nouvelleHeure;
        this.effectuee = false;
        this.observations = "Reprogrammée";
    }
    
    public boolean estEnRetard() {
        return !effectuee && date.isBefore(LocalDate.now());
    }
    
    public boolean estPlanifieePourAujourdhui() {
        return date.equals(LocalDate.now());
    }
    
    public boolean estPlanifieePourSemaine() {
        LocalDate aujourdhui = LocalDate.now();
        LocalDate finSemaine = aujourdhui.plusDays(7);
        return !date.isBefore(aujourdhui) && !date.isAfter(finSemaine);
    }
    
    public String getStatut() {
        if (effectuee) return "Effectuée";
        if (estEnRetard()) return "En retard";
        if (estPlanifieePourAujourdhui()) return "Aujourd'hui";
        return "Planifiée";
    }
    
}