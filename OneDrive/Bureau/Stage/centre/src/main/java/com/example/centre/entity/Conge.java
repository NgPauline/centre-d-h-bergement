/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "conges")
public class Conge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personnel_id", nullable = false)
    private Personnel personnel;
    
    @Column(nullable = false)
    private LocalDate dateDebut;
    
    @Column(nullable = false)
    private LocalDate dateFin;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeConge type;
    
    @Enumerated(EnumType.STRING)
    private StatutDemande statut = StatutDemande.EN_ATTENTE;
    
    private String motif;
    private String commentaires;
    private LocalDate dateDemande = LocalDate.now();
    private LocalDate dateValidation;
    private String validateur;
    
    // Constructeurs
    public Conge() {}
    
    public Conge(Personnel personnel, LocalDate dateDebut, LocalDate dateFin, TypeConge type) {
        this.personnel = personnel;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.type = type;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Personnel getPersonnel() { return personnel; }
    public void setPersonnel(Personnel personnel) { this.personnel = personnel; }
    
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    
    public TypeConge getType() { return type; }
    public void setType(TypeConge type) { this.type = type; }
    
    public StatutDemande getStatut() { return statut; }
    public void setStatut(StatutDemande statut) { this.statut = statut; }
    
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
    
    public String getCommentaires() { return commentaires; }
    public void setCommentaires(String commentaires) { this.commentaires = commentaires; }
    
    public LocalDate getDateDemande() { return dateDemande; }
    public void setDateDemande(LocalDate dateDemande) { this.dateDemande = dateDemande; }
    
    public LocalDate getDateValidation() { return dateValidation; }
    public void setDateValidation(LocalDate dateValidation) { this.dateValidation = dateValidation; }
    
    public String getValidateur() { return validateur; }
    public void setValidateur(String validateur) { this.validateur = validateur; }
    
     // Méthodes métier
    public void approuver(String validateur) {
        this.statut = StatutDemande.APPROUVEE;
        this.validateur = validateur;
        this.dateValidation = LocalDate.now();
    }
    
    public void refuser(String motif) {
        this.statut = StatutDemande.REFUSEE;
        this.commentaires = motif;
        this.dateValidation = LocalDate.now();
    }
    
    public int getDuree() {
        return Period.between(dateDebut, dateFin).getDays() + 1;
    }
    
    public boolean estEnCours() {
        LocalDate aujourdhui = LocalDate.now();
        return !aujourdhui.isBefore(dateDebut) && !aujourdhui.isAfter(dateFin);
    }
    
    public boolean estPasse() {
        return dateFin.isBefore(LocalDate.now());
    }
    
    public boolean estAVenir() {
        return dateDebut.isAfter(LocalDate.now());
    }
    
    public boolean estApprouve() {
        return statut == StatutDemande.APPROUVEE;
    }
    
    public boolean estEnAttente() {
        return statut == StatutDemande.EN_ATTENTE;
    }
    
    public long getJoursRestants() {
        if (estPasse()) return 0;
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), dateDebut);
    }
    
}
