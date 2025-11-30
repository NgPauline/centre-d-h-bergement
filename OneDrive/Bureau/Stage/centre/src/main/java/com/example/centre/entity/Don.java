/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "dons")
public class Don {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String numeroDon;
    
    @Enumerated(EnumType.STRING)
    private TypeDonateur typeDonateur;
    
    private String nomDonateur;
    private String prenomDonateur;
    private String organisationDonateur;
    private String email;
    private String telephone;
    
    @Embedded
    private Adresse adresse;
    
    private Double montant;
    private String descriptionDon;
    
    @Column(nullable = false)
    private LocalDate dateDon;
    
    @Enumerated(EnumType.STRING)
    private ModePaiement modePaiement;
    
    @Enumerated(EnumType.STRING)
    private TypeDon typeDon;
    
    private Boolean recuFiscalDemande = false;
    private Boolean recuFiscalEnvoye = false;
    private LocalDate dateEnvoiRecu;
    private String reference;
    private String message;
    private String notes;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comptable_id")
    private Personnel comptable;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campagne_id")
    private CampagneDon campagne;
    
    // Constructeurs
    public Don() {
        this.numeroDon = "DON-" + System.currentTimeMillis();
        this.dateDon = LocalDate.now();
    }
    
    public Don(TypeDonateur typeDonateur, Double montant, TypeDon typeDon) {
        this();
        this.typeDonateur = typeDonateur;
        this.montant = montant;
        this.typeDon = typeDon;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNumeroDon() { return numeroDon; }
    public void setNumeroDon(String numeroDon) { this.numeroDon = numeroDon; }
    
    public TypeDonateur getTypeDonateur() { return typeDonateur; }
    public void setTypeDonateur(TypeDonateur typeDonateur) { this.typeDonateur = typeDonateur; }
    
    public String getNomDonateur() { return nomDonateur; }
    public void setNomDonateur(String nomDonateur) { this.nomDonateur = nomDonateur; }
    
    public String getPrenomDonateur() { return prenomDonateur; }
    public void setPrenomDonateur(String prenomDonateur) { this.prenomDonateur = prenomDonateur; }
    
    public String getOrganisationDonateur() { return organisationDonateur; }
    public void setOrganisationDonateur(String organisationDonateur) { this.organisationDonateur = organisationDonateur; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    public Adresse getAdresse() { return adresse; }
    public void setAdresse(Adresse adresse) { this.adresse = adresse; }
    
    public Double getMontant() { return montant; }
    public void setMontant(Double montant) { this.montant = montant; }
    
    public String getDescriptionDon() { return descriptionDon; }
    public void setDescriptionDon(String descriptionDon) { this.descriptionDon = descriptionDon; }
    
    public LocalDate getDateDon() { return dateDon; }
    public void setDateDon(LocalDate dateDon) { this.dateDon = dateDon; }
    
    public ModePaiement getModePaiement() { return modePaiement; }
    public void setModePaiement(ModePaiement modePaiement) { this.modePaiement = modePaiement; }
    
    public TypeDon getTypeDon() { return typeDon; }
    public void setTypeDon(TypeDon typeDon) { this.typeDon = typeDon; }
    
    public Boolean getRecuFiscalDemande() { return recuFiscalDemande; }
    public void setRecuFiscalDemande(Boolean recuFiscalDemande) { this.recuFiscalDemande = recuFiscalDemande; }
    
    public Boolean getRecuFiscalEnvoye() { return recuFiscalEnvoye; }
    public void setRecuFiscalEnvoye(Boolean recuFiscalEnvoye) { this.recuFiscalEnvoye = recuFiscalEnvoye; }
    
    public LocalDate getDateEnvoiRecu() { return dateEnvoiRecu; }
    public void setDateEnvoiRecu(LocalDate dateEnvoiRecu) { this.dateEnvoiRecu = dateEnvoiRecu; }
    
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public Personnel getComptable() { return comptable; }
    public void setComptable(Personnel comptable) { this.comptable = comptable; }
    
    public CampagneDon getCampagne() { return campagne; }
    public void setCampagne(CampagneDon campagne) { this.campagne = campagne; }
    
     // Méthodes métier
    public String genererRecuFiscal() {
        if (montant == null || montant <= 0) return null;
        
        this.recuFiscalDemande = true;
        String numeroRecu = "RF-" + numeroDon + "-" + LocalDate.now().getYear();
        
        // Logique de génération du reçu fiscal
        return numeroRecu;
    }
    
    public void envoyerRemerciement() {
        if (email != null && !email.isEmpty()) {
            // Implémentation de l'envoi d'email de remerciement
            System.out.println("Email de remerciement envoyé à: " + email);
        }
        if (telephone != null && !telephone.isEmpty()) {
            // Implémentation de l'envoi de SMS
            System.out.println("SMS de remerciement envoyé à: " + telephone);
        }
    }
    
    public void marquerRecuEnvoye() {
        this.recuFiscalEnvoye = true;
        this.dateEnvoiRecu = LocalDate.now();
    }
    
    public String getNomCompletDonateur() {
        if (typeDonateur == TypeDonateur.PARTICULIER) {
            return prenomDonateur + " " + nomDonateur;
        } else {
            return organisationDonateur;
        }
    }
    
    public boolean estEligibleRecuFiscal() {
        return montant != null && montant >= 40.0 && typeDon == TypeDon.FINANCIER;
    }
    
    public boolean estAnonyme() {
        return typeDonateur == TypeDonateur.ANONYME;
    }
    
}