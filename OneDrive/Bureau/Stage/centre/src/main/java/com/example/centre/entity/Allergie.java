/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "allergies")
public class Allergie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String substance;
    
    private String typeReaction;
    
    @Enumerated(EnumType.STRING)
    private NiveauGravite gravite;
    
    private String traitement;
    private String observations;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dossier_medical_id")
    private DossierMedical dossierMedical;
    
    // Constructeurs
    public Allergie() {}
    
    public Allergie(String substance, NiveauGravite gravite) {
        this.substance = substance;
        this.gravite = gravite;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getSubstance() { return substance; }
    public void setSubstance(String substance) { this.substance = substance; }
    
    public String getTypeReaction() { return typeReaction; }
    public void setTypeReaction(String typeReaction) { this.typeReaction = typeReaction; }
    
    public NiveauGravite getGravite() { return gravite; }
    public void setGravite(NiveauGravite gravite) { this.gravite = gravite; }
    
    public String getTraitement() { return traitement; }
    public void setTraitement(String traitement) { this.traitement = traitement; }
    
    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
    
    public DossierMedical getDossierMedical() { return dossierMedical; }
    public void setDossierMedical(DossierMedical dossierMedical) { this.dossierMedical = dossierMedical; }
    
    // Méthodes métier
    public boolean estCritique() {
        return gravite == NiveauGravite.CRITIQUE || gravite == NiveauGravite.SEVERE;
    }
    
    public String getInfoComplete() {
        return substance + " - " + gravite + " - " + typeReaction;
    }
    
}