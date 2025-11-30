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
@Table(name = "factures")
public class Facture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String numero;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_id", nullable = false)
    private Resident resident;
    
    @Column(nullable = false)
    private LocalDate dateEmission;
    
    @Column(nullable = false)
    private LocalDate dateEcheance;
    
    private Double montantTotal;
    private Double montantDejaPaye = 0.0;
    
    @Enumerated(EnumType.STRING)
    private StatutFacture statut = StatutFacture.BROUILLON;
    
    private String referencePaiement;
    private String notes;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comptable_id")
    private Personnel comptable;
    
    @OneToMany(mappedBy = "facture", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<LigneFacture> lignes = new ArrayList<>();
    
    @OneToMany(mappedBy = "facture", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Paiement> paiements = new ArrayList<>();
    
    // Constructeurs
    public Facture() {}
    
    public Facture(String numero, Resident resident, LocalDate dateEmission, LocalDate dateEcheance) {
        this.numero = numero;
        this.resident = resident;
        this.dateEmission = dateEmission;
        this.dateEcheance = dateEcheance;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public Resident getResident() { return resident; }
    public void setResident(Resident resident) { this.resident = resident; }
    
    public LocalDate getDateEmission() { return dateEmission; }
    public void setDateEmission(LocalDate dateEmission) { this.dateEmission = dateEmission; }
    
    public LocalDate getDateEcheance() { return dateEcheance; }
    public void setDateEcheance(LocalDate dateEcheance) { this.dateEcheance = dateEcheance; }
    
    public Double getMontantTotal() { return montantTotal; }
    public void setMontantTotal(Double montantTotal) { this.montantTotal = montantTotal; }
    
    public Double getMontantDejaPaye() { return montantDejaPaye; }
    public void setMontantDejaPaye(Double montantDejaPaye) { this.montantDejaPaye = montantDejaPaye; }
    
    public StatutFacture getStatut() { return statut; }
    public void setStatut(StatutFacture statut) { this.statut = statut; }
    
    public String getReferencePaiement() { return referencePaiement; }
    public void setReferencePaiement(String referencePaiement) { this.referencePaiement = referencePaiement; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public Personnel getComptable() { return comptable; }
    public void setComptable(Personnel comptable) { this.comptable = comptable; }
    
    public List<LigneFacture> getLignes() { return lignes; }
    public void setLignes(List<LigneFacture> lignes) { this.lignes = lignes; }
    
    public List<Paiement> getPaiements() { return paiements; }
    public void setPaiements(List<Paiement> paiements) { this.paiements = paiements; }
    
    // Méthodes métier
    public Double calculerTotal() {
        this.montantTotal = lignes.stream()
                .mapToDouble(LigneFacture::getMontant)
                .sum();
        return montantTotal;
    }
    
    public void marquerPayee() {
        this.statut = StatutFacture.PAYEE;
        this.montantDejaPaye = montantTotal;
    }
    
    public void marquerPartiellementPayee(Double montantPaye) {
        this.statut = StatutFacture.PARTIELLEMENT_PAYEE;
        this.montantDejaPaye = montantPaye;
    }
    
    public void marquerEnRetard() {
        if (LocalDate.now().isAfter(dateEcheance)){
            this.statut = StatutFacture.EN_RETARD;
        }
    }
    
    public Double getMontantRestant() {
        return montantTotal - montantDejaPaye;
    }
    
    public boolean estPayee() {
        return statut == StatutFacture.PAYEE;
    }
    
    public boolean estEnRetard() {
        return LocalDate.now().isAfter(dateEcheance) && !estPayee();
    }
    
    public long getJoursRetard() {
        if (!estEnRetard()) return 0;
        return java.time.temporal.ChronoUnit.DAYS.between(dateEcheance, LocalDate.now());
    }
    
    public void ajouterLigne(String description, Integer quantite, Double prixUnitaire, TypeService typeService) {
        LigneFacture ligne = new LigneFacture();
        ligne.setDescription(description);
        ligne.setQuantite(quantite);
        ligne.setPrixUnitaire(prixUnitaire);
        ligne.setTypeService(typeService);
        ligne.setFacture(this);
        lignes.add(ligne);
        calculerTotal();
    }
    
    public String genererPDF() {
        // Simulation de génération PDF
        return "PDF_Facture_" + numero + ".pdf";
    }
    
}
