/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "paiements")
public class Paiement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facture_id", nullable = false)
    private Facture facture;
    
    @Column(nullable = false)
    private LocalDate datePaiement;
    
    @Column(nullable = false)
    private Double montant;
    
    @Enumerated(EnumType.STRING)
    private ModePaiement mode;
    
    private String reference;
    private String notes;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comptable_id")
    private Personnel comptable;
    
    // Constructeurs
    public Paiement() {}
    
    public Paiement(Facture facture, Double montant, ModePaiement mode) {
        this.facture = facture;
        this.montant = montant;
        this.mode = mode;
        this.datePaiement = LocalDate.now();
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Facture getFacture() { return facture; }
    public void setFacture(Facture facture) { this.facture = facture; }
    
    public LocalDate getDatePaiement() { return datePaiement; }
    public void setDatePaiement(LocalDate datePaiement) { this.datePaiement = datePaiement; }
    
    public Double getMontant() { return montant; }
    public void setMontant(Double montant) { this.montant = montant; }
    
    public ModePaiement getMode() { return mode; }
    public void setMode(ModePaiement mode) { this.mode = mode; }
    
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public Personnel getComptable() { return comptable; }
    public void setComptable(Personnel comptable) { this.comptable = comptable; }
    
    
    // Méthodes métier
    public void enregistrerPaiement(String reference, String notes) {
        this.reference = reference;
        this.notes = notes;
        
        // Mettre à jour le statut de la facture
        Double nouveauTotalPaye = facture.getMontantDejaPaye() + montant;
        
        if (nouveauTotalPaye >= facture.getMontantTotal()) {
            facture.marquerPayee();
        } else {
            facture.marquerPartiellementPayee(nouveauTotalPaye);
        }
    }
    
    public boolean estComplet() {
        return reference != null && !reference.isEmpty();
    }
    
    public String getInfoPaiement() {
        return mode + " - " + montant + "€ - " + datePaiement;
    }
    
}
