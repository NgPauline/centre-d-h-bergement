/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ecritures_comptables")
public class EcritureComptable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String numero;
    
    @Column(nullable = false)
    private LocalDate date;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeEcriture type;
    
    @Column(nullable = false)
    private String libelle;
    
    @Column(nullable = false)
    private Double montant;
    
    @Column(nullable = false)
    private String compte;
    
    private String reference;
    private String contrepartie;
    private Boolean valide = false;
    private String notes;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comptable_id")
    private Personnel comptable;
    
    // Constructeurs
    public EcritureComptable() {
        this.numero = "ECR-" + System.currentTimeMillis();
        this.date = LocalDate.now();
    }
    
    public EcritureComptable(TypeEcriture type, String libelle, Double montant, String compte) {
        this();
        this.type = type;
        this.libelle = libelle;
        this.montant = montant;
        this.compte = compte;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public TypeEcriture getType() { return type; }
    public void setType(TypeEcriture type) { this.type = type; }
    
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    
    public Double getMontant() { return montant; }
    public void setMontant(Double montant) { this.montant = montant; }
    
    public String getCompte() { return compte; }
    public void setCompte(String compte) { this.compte = compte; }
    
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    
    public String getContrepartie() { return contrepartie; }
    public void setContrepartie(String contrepartie) { this.contrepartie = contrepartie; }
    
    public Boolean getValide() { return valide; }
    public void setValide(Boolean valide) { this.valide = valide; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public Personnel getComptable() { return comptable; }
    public void setComptable(Personnel comptable) { this.comptable = comptable; }
    
    // Méthodes métier
    public void valider() {
        this.valide = true;
    }
    
    public void invalider() {
        this.valide = false;
    }
    
    public String genererRapport() {
        return "Écriture " + numero + " - " + type + " - " + montant + "€ - " + libelle;
    }
    
    public boolean estUneRecette() {
        return type == TypeEcriture.RECETTE;
    }
    
    public boolean estUneDepense() {
        return type == TypeEcriture.DEPENSE;
    }
    
    public Double getMontantSigne() {
        return estUneDepense() ? -montant : montant;
    }
    
    public boolean estDuMois(LocalDate dateReference) {
        return date.getMonth() == dateReference.getMonth() && 
               date.getYear() == dateReference.getYear();
    }
    
    public boolean estDuTrimestre(LocalDate dateReference) {
        int trimestre = (date.getMonthValue() - 1) / 3 + 1;
        int trimestreRef = (dateReference.getMonthValue() - 1) / 3 + 1;
        return trimestre == trimestreRef && date.getYear() == dateReference.getYear();
    }
    
}
