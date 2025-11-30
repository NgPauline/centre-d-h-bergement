/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "lignes_facture")
public class LigneFacture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String description;
    
    private Integer quantite = 1;
    private Double prixUnitaire;
    private Double montant;
    
    @Enumerated(EnumType.STRING)
    private TypeService typeService;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facture_id", nullable = false)
    private Facture facture;
    
    // Constructeurs
    public LigneFacture() {}
    
    public LigneFacture(String description, Double prixUnitaire, TypeService typeService) {
        this.description = description;
        this.prixUnitaire = prixUnitaire;
        this.typeService = typeService;
        this.montant = calculerMontant();
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }
    
    public Double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(Double prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    
    public Double getMontant() { 
        if (montant == null) {
            calculerMontant();
        }
        return montant; 
    }
    public void setMontant(Double montant) { this.montant = montant; }
    
    public TypeService getTypeService() { return typeService; }
    public void setTypeService(TypeService typeService) { this.typeService = typeService; }
    
    public Facture getFacture() { return facture; }
    public void setFacture(Facture facture) { this.facture = facture; }
    
    
    // Méthodes métier
    public Double calculerMontant() {
        this.montant = prixUnitaire * quantite;
        return montant;
    }
    
    public void mettreAJourQuantite(Integer nouvelleQuantite) {
        this.quantite = nouvelleQuantite;
        calculerMontant();
    }
    
    public void mettreAJourPrix(Double nouveauPrix) {
        this.prixUnitaire = nouveauPrix;
        calculerMontant();
    }
    
}