/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "contacts_urgence")
public class ContactUrgence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false)
    private String prenom;
    
    private String telephone;
    private String email;
    private String lienParente;
    private Boolean contactPrincipal = false;
    
    @Embedded
    private Adresse adresse;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_id", nullable = false)
    private Resident resident;
    
    // Constructeurs
    public ContactUrgence() {}
    
    public ContactUrgence(String nom, String prenom, String telephone, String lienParente) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.lienParente = lienParente;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getLienParente() { return lienParente; }
    public void setLienParente(String lienParente) { this.lienParente = lienParente; }
    
    public Boolean getContactPrincipal() { return contactPrincipal; }
    public void setContactPrincipal(Boolean contactPrincipal) { this.contactPrincipal = contactPrincipal; }
    
    public Adresse getAdresse() { return adresse; }
    public void setAdresse(Adresse adresse) { this.adresse = adresse; }
    
    public Resident getResident() { return resident; }
    public void setResident(Resident resident) { this.resident = resident; }
    
    // Méthodes métier
    public String getNomComplet() {
        return prenom + " " + nom;
    }
    
    public void notifier(String message) {
        // Implémentation de la notification (SMS, email, etc.)
        System.out.println("Notification à " + getNomComplet() + ": " + message);
    }
    
}