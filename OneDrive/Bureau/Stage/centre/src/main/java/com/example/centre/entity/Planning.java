/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;

@Entity
@Table(name = "plannings")
public class Planning {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personnel_id", nullable = false)
    private Personnel personnel;
    
    @Column(nullable = false)
    private LocalDate date;
    
    @Column(nullable = false)
    private LocalTime heureDebut;
    
    @Column(nullable = false)
    private LocalTime heureFin;
    
    @Enumerated(EnumType.STRING)
    private TypeShift shift;
    
    private String activite;
    private String lieu;
    private Boolean valide = false;
    private String notes;
    
    // Constructeurs
    public Planning() {}
    
    public Planning(Personnel personnel, LocalDate date, LocalTime heureDebut, LocalTime heureFin) {
        this.personnel = personnel;
        this.date = date;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        determinerShift();
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Personnel getPersonnel() { return personnel; }
    public void setPersonnel(Personnel personnel) { this.personnel = personnel; }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public LocalTime getHeureDebut() { return heureDebut; }
    public void setHeureDebut(LocalTime heureDebut) { this.heureDebut = heureDebut; }
    
    public LocalTime getHeureFin() { return heureFin; }
    public void setHeureFin(LocalTime heureFin) { this.heureFin = heureFin; }
    
    public TypeShift getShift() { return shift; }
    public void setShift(TypeShift shift) { this.shift = shift; }
    
    public String getActivite() { return activite; }
    public void setActivite(String activite) { this.activite = activite; }
    
    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }
    
    public Boolean getValide() { return valide; }
    public void setValide(Boolean valide) { this.valide = valide; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    
    // Méthodes métier
    public void determinerShift() {
        if (heureDebut.isBefore(LocalTime.of(12, 0))) {
            this.shift = TypeShift.MATIN;
        } else if (heureDebut.isBefore(LocalTime.of(18, 0))) {
            this.shift = TypeShift.APRES_MIDI;
        } else {
            this.shift = TypeShift.NUIT;
        }
    }
    
    public boolean verifierDisponibilite() {
        // Vérifier les conflits avec d'autres plannings
        return personnel.estDisponible();
    }
    
    public Double calculerHeures() {
        Duration duree = Duration.between(heureDebut, heureFin);
        return duree.toMinutes() / 60.0;
    }
    
    public boolean estPourAujourdhui() {
        return date.equals(LocalDate.now());
    }
    
    public boolean estPasse() {
        return date.isBefore(LocalDate.now()) || 
               (date.equals(LocalDate.now()) && heureFin.isBefore(LocalTime.now()));
    }
    
    public boolean estEnCours() {
        return date.equals(LocalDate.now()) && 
               heureDebut.isBefore(LocalTime.now()) && 
               heureFin.isAfter(LocalTime.now());
    }
    
    public boolean estValide() {
        return valide && verifierDisponibilite();
    }
    
    public void valider() {
        this.valide = true;
    }
    
}