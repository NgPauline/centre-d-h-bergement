/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "budgets")
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Integer annee;
    
    @Column(nullable = false)
    private String categorie;
    
    @Column(nullable = false)
    private Double montantPrevu;
    
    private Double montantRealise = 0.0;
    private String description;
    private String codeComptable;
    private LocalDate dateCreation = LocalDate.now();
    
    // Constructeurs
    public Budget() {}
    
    public Budget(Integer annee, String categorie, Double montantPrevu) {
        this.annee = annee;
        this.categorie = categorie;
        this.montantPrevu = montantPrevu;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Integer getAnnee() { return annee; }
    public void setAnnee(Integer annee) { this.annee = annee; }
    
    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    
    public Double getMontantPrevu() { return montantPrevu; }
    public void setMontantPrevu(Double montantPrevu) { this.montantPrevu = montantPrevu; }
    
    public Double getMontantRealise() { return montantRealise; }
    public void setMontantRealise(Double montantRealise) { this.montantRealise = montantRealise; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCodeComptable() { return codeComptable; }
    public void setCodeComptable(String codeComptable) { this.codeComptable = codeComptable; }
    
    public LocalDate getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDate dateCreation) { this.dateCreation = dateCreation; }
    
    
    // Méthodes métier
    public Double calculerEcart() {
        return montantRealise - montantPrevu;
    }
    
    public Double calculerEcartPourcentage() {
        if (montantPrevu == 0) return 0.0;
        return (calculerEcart() / montantPrevu) * 100;
    }
    
    public boolean estRespecte() {
        return calculerEcart() <= 0; // Dépenses <= prévisions
    }
    
    public boolean estDepasse() {
        return calculerEcart() > 0;
    }
    
    public Double getTauxRealisation() {
        if (montantPrevu == 0) return 0.0;
        return (montantRealise / montantPrevu) * 100;
    }
    
    public void mettreAJourRealisation(Double nouveauMontant) {
        this.montantRealise = nouveauMontant;
    }
    
    public void ajouterRealisation(Double montant) {
        this.montantRealise += montant;
    }
    
    public String getStatutBudget() {
        double pourcentage = getTauxRealisation();
        if (pourcentage <= 80) return "Sous-utilisé";
        if (pourcentage <= 100) return "Normal";
        return "Dépassé";
    }
     
}
