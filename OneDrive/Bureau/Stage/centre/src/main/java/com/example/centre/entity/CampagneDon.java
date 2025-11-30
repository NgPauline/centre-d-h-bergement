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
@Table(name = "campagnes_dons")
public class CampagneDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String titre;
    
    private String description;
    private String objectif;
    
    @Column(nullable = false)
    private LocalDate dateDebut;
    
    @Column(nullable = false)
    private LocalDate dateFin;
    
    private Double objectifMontant;
    private Double montantCollecte = 0.0;
    private Boolean active = true;
    private String imageUrl;
    private String couleurTheme;
    private String messageRemerciement;
    
    @OneToMany(mappedBy = "campagne", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Don> dons = new ArrayList<>();
    
    // Constructeurs
    public CampagneDon() {}
    
    public CampagneDon(String titre, String description, LocalDate dateDebut, LocalDate dateFin) {
        this.titre = titre;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getObjectif() { return objectif; }
    public void setObjectif(String objectif) { this.objectif = objectif; }
    
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    
    public Double getObjectifMontant() { return objectifMontant; }
    public void setObjectifMontant(Double objectifMontant) { this.objectifMontant = objectifMontant; }
    
    public Double getMontantCollecte() { return montantCollecte; }
    public void setMontantCollecte(Double montantCollecte) { this.montantCollecte = montantCollecte; }
    
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public String getCouleurTheme() { return couleurTheme; }
    public void setCouleurTheme(String couleurTheme) { this.couleurTheme = couleurTheme; }
    
    public String getMessageRemerciement() { return messageRemerciement; }
    public void setMessageRemerciement(String messageRemerciement) { this.messageRemerciement = messageRemerciement; }
    
    public List<Don> getDons() { return dons; }
    public void setDons(List<Don> dons) { this.dons = dons; }
    
     // Méthodes métier
    public Double calculerProgression() {
        if (objectifMontant == null || objectifMontant == 0) return 0.0;
        return (montantCollecte / objectifMontant) * 100;
    }
    
    public void cloturerCampagne() {
        this.active = false;
    }
    
    public void reactiverCampagne() {
        this.active = true;
    }
    
    public void ajouterDon(Don don) {
        dons.add(don);
        don.setCampagne(this);
        mettreAJourMontantCollecte();
    }
    
    public void mettreAJourMontantCollecte() {
        this.montantCollecte = dons.stream()
                .mapToDouble(Don::getMontant)
                .sum();
    }
    
    public boolean estEnCours() {
        LocalDate aujourdhui = LocalDate.now();
        return active && !aujourdhui.isBefore(dateDebut) && !aujourdhui.isAfter(dateFin);
    }
    
    public boolean estTerminee() {
        return !active || LocalDate.now().isAfter(dateFin);
    }
    
    public boolean estAVenir() {
        return LocalDate.now().isBefore(dateDebut);
    }
    
    public long getJoursRestants() {
        if (estTerminee()) return 0;
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), dateFin);
    }
    
    public Double getMontantRestant() {
        if (objectifMontant == null) return 0.0;
        return objectifMontant - montantCollecte;
    }
    
    public int getNombreDons() {
        return dons.size();
    }
    
}
