/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appels_urgence")
public class AppelUrgence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_id", nullable = false)
    private Resident resident;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personnel_id", nullable = false)
    private Personnel appelant;
    
    @Column(nullable = false)
    private LocalDateTime dateHeure;
    
    @Enumerated(EnumType.STRING)
    private TypeUrgence type;
    
    private Boolean ambulanceAppelee = false;
    private String numeroAmbulance;
    private String description;
    private String mesuresPrises;
    private String destination;
    private String contactFamille;
    private Boolean familleNotifiee = false;
    private String etatResident;
    private LocalDateTime dateHeureAmbulance;
    private String suivi;
    
    // CORRECTION : Ajouter les attributs manquants
    private Boolean cloture = false;
    private LocalDateTime dateCloture;
    private String suitesDonnees;
    private String messageFamille;
    private LocalDateTime dateNotificationFamille;
    
    // Constructeurs
    public AppelUrgence() {}
    
    public AppelUrgence(Resident resident, Personnel appelant, TypeUrgence type) {
        this.resident = resident;
        this.appelant = appelant;
        this.type = type;
        this.dateHeure = LocalDateTime.now();
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Resident getResident() { return resident; }
    public void setResident(Resident resident) { this.resident = resident; }
    
    public Personnel getAppelant() { return appelant; }
    public void setAppelant(Personnel appelant) { this.appelant = appelant; }
    
    public LocalDateTime getDateHeure() { return dateHeure; }
    public void setDateHeure(LocalDateTime dateHeure) { this.dateHeure = dateHeure; }
    
    public TypeUrgence getType() { return type; }
    public void setType(TypeUrgence type) { this.type = type; }
    
    public Boolean getAmbulanceAppelee() { return ambulanceAppelee; }
    public void setAmbulanceAppelee(Boolean ambulanceAppelee) { this.ambulanceAppelee = ambulanceAppelee; }
    
    public String getNumeroAmbulance() { return numeroAmbulance; }
    public void setNumeroAmbulance(String numeroAmbulance) { this.numeroAmbulance = numeroAmbulance; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getMesuresPrises() { return mesuresPrises; }
    public void setMesuresPrises(String mesuresPrises) { this.mesuresPrises = mesuresPrises; }
    
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    
    public String getContactFamille() { return contactFamille; }
    public void setContactFamille(String contactFamille) { this.contactFamille = contactFamille; }
    
    public Boolean getFamilleNotifiee() { return familleNotifiee; }
    public void setFamilleNotifiee(Boolean familleNotifiee) { this.familleNotifiee = familleNotifiee; }
    
    public String getEtatResident() { return etatResident; }
    public void setEtatResident(String etatResident) { this.etatResident = etatResident; }
    
    public LocalDateTime getDateHeureAmbulance() { return dateHeureAmbulance; }
    public void setDateHeureAmbulance(LocalDateTime dateHeureAmbulance) { this.dateHeureAmbulance = dateHeureAmbulance; }
    
    public String getSuivi() { return suivi; }
    public void setSuivi(String suivi) { this.suivi = suivi; }
    
    // CORRECTION : Ajouter les getters et setters pour les nouveaux attributs
    public Boolean getCloture() { return cloture; }
    public void setCloture(Boolean cloture) { this.cloture = cloture; }
    
    public LocalDateTime getDateCloture() { return dateCloture; }
    public void setDateCloture(LocalDateTime dateCloture) { this.dateCloture = dateCloture; }
    
    public String getSuitesDonnees() { return suitesDonnees; }
    public void setSuitesDonnees(String suitesDonnees) { this.suitesDonnees = suitesDonnees; }
    
    public String getMessageFamille() { return messageFamille; }
    public void setMessageFamille(String messageFamille) { this.messageFamille = messageFamille; }
    
    public LocalDateTime getDateNotificationFamille() { return dateNotificationFamille; }
    public void setDateNotificationFamille(LocalDateTime dateNotificationFamille) { this.dateNotificationFamille = dateNotificationFamille; }
    
    // Méthodes métier
    public void enregistrerAppel(String description, String mesuresPrises) {
        this.description = description;
        this.mesuresPrises = mesuresPrises;
    }
    
    public void appelerAmbulance(String numero, String destination) {
        this.ambulanceAppelee = true;
        this.numeroAmbulance = numero;
        this.destination = destination;
        this.dateHeureAmbulance = LocalDateTime.now();
    }
    
    public void notifierFamille(String contact, String message) {
        this.contactFamille = contact;
        this.familleNotifiee = true;
        this.messageFamille = message;
        this.dateNotificationFamille = LocalDateTime.now();
        // Implémentation de l'envoi de notification
        System.out.println("Notification famille: " + contact + " - " + message);
    }
    
    // CORRECTION : Ajouter la méthode cloturerIncident()
    public void cloturerIncident(String suitesDonnees) {
        this.cloture = true;
        this.suitesDonnees = suitesDonnees;
        this.dateCloture = LocalDateTime.now();
        this.suivi = "Incident clôturé - " + suitesDonnees;
    }
    
    public boolean estResolu() {
        return cloture || (suivi != null && (suivi.contains("résolu") || suivi.contains("terminé") || suivi.contains("clôturé")));
    }
    
    public long getDureeIntervention() {
        if (dateHeureAmbulance == null) return 0;
        return java.time.temporal.ChronoUnit.MINUTES.between(dateHeure, dateHeureAmbulance);
    }
    
    public String getResumeUrgence() {
        return type + " - " + resident.getNomComplet() + " - " + dateHeure.toLocalDate();
    }
}