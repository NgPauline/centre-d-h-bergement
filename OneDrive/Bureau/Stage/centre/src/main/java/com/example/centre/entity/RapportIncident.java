/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rapports_incidents")
public class RapportIncident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_id")
    private Resident resident;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "declarant_id", nullable = false)
    private Personnel declarant;
    
    @Column(nullable = false)
    private LocalDateTime dateHeure;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeIncident type;
    
    @Enumerated(EnumType.STRING)
    private NiveauGravite gravite;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    
    private String mesuresPrises;
    private Boolean familleInformee = false;
    private String contactFamille;
    private String temoins;
    private String suitesDonnees;
    private Boolean cloture = false;
    private LocalDateTime dateCloture;
    
    // Constructeurs
    public RapportIncident() {
        this.dateHeure = LocalDateTime.now();
    }
    
    public RapportIncident(Personnel declarant, TypeIncident type, String description) {
        this();
        this.declarant = declarant;
        this.type = type;
        this.description = description;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Resident getResident() { return resident; }
    public void setResident(Resident resident) { this.resident = resident; }
    
    public Personnel getDeclarant() { return declarant; }
    public void setDeclarant(Personnel declarant) { this.declarant = declarant; }
    
    public LocalDateTime getDateHeure() { return dateHeure; }
    public void setDateHeure(LocalDateTime dateHeure) { this.dateHeure = dateHeure; }
    
    public TypeIncident getType() { return type; }
    public void setType(TypeIncident type) { this.type = type; }
    
    public NiveauGravite getGravite() { return gravite; }
    public void setGravite(NiveauGravite gravite) { this.gravite = gravite; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getMesuresPrises() { return mesuresPrises; }
    public void setMesuresPrises(String mesuresPrises) { this.mesuresPrises = mesuresPrises; }
    
    public Boolean getFamilleInformee() { return familleInformee; }
    public void setFamilleInformee(Boolean familleInformee) { this.familleInformee = familleInformee; }
    
    public String getContactFamille() { return contactFamille; }
    public void setContactFamille(String contactFamille) { this.contactFamille = contactFamille; }
    
    public String getTemoins() { return temoins; }
    public void setTemoins(String temoins) { this.temoins = temoins; }
    
    public String getSuitesDonnees() { return suitesDonnees; }
    public void setSuitesDonnees(String suitesDonnees) { this.suitesDonnees = suitesDonnees; }
    
    public Boolean getCloture() { return cloture; }
    public void setCloture(Boolean cloture) { this.cloture = cloture; }
    
    public LocalDateTime getDateCloture() { return dateCloture; }
    public void setDateCloture(LocalDateTime dateCloture) { this.dateCloture = dateCloture; }
    
    // Méthodes métier
    public void notifierDirection() {
        // Logique de notification à la direction
        System.out.println("Incident notifié à la direction: " + type + " - " + gravite);
    }
    
    public void cloturerIncident(String suitesDonnees) {
        this.cloture = true;
        this.suitesDonnees = suitesDonnees;
        this.dateCloture = LocalDateTime.now();
    }
    
    public void rouvrirIncident() {
        this.cloture = false;
        this.dateCloture = null;
    }
    
    public boolean estGrave() {
        return gravite == NiveauGravite.SEVERE || gravite == NiveauGravite.CRITIQUE;
    }
    
    public boolean estRecent() {
        return dateHeure.isAfter(LocalDateTime.now().minusDays(7));
    }
    
    public void informerFamille(String contact, String message) {
        this.familleInformee = true;
        this.contactFamille = contact;
        // Implémentation de l'envoi de notification
        System.out.println("Famille informée (" + contact + "): " + message);
    }
    
    public String getResumeIncident() {
        return type + " - " + resident.getNomComplet() + " - " + dateHeure.toLocalDate();
    }
    
}
