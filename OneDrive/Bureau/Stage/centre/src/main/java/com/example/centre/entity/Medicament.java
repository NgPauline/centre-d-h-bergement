/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medicaments")
public class Medicament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    private String principeActif;
    private String forme;
    private Double dosage;
    private String unite;
    private String laboratoire;
    private String codeATC;
    
    @OneToMany(mappedBy = "medicament", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Prescription> prescriptions = new ArrayList<>();
    
    // Constructeurs
    public Medicament() {}
    
    public Medicament(String nom, String forme, Double dosage) {
        this.nom = nom;
        this.forme = forme;
        this.dosage = dosage;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrincipeActif() { return principeActif; }
    public void setPrincipeActif(String principeActif) { this.principeActif = principeActif; }
    
    public String getForme() { return forme; }
    public void setForme(String forme) { this.forme = forme; }
    
    public Double getDosage() { return dosage; }
    public void setDosage(Double dosage) { this.dosage = dosage; }
    
    public String getUnite() { return unite; }
    public void setUnite(String unite) { this.unite = unite; }
    
    public String getLaboratoire() { return laboratoire; }
    public void setLaboratoire(String laboratoire) { this.laboratoire = laboratoire; }
    
    public String getCodeATC() { return codeATC; }
    public void setCodeATC(String codeATC) { this.codeATC = codeATC; }
    
    public List<Prescription> getPrescriptions() { return prescriptions; }
    public void setPrescriptions(List<Prescription> prescriptions) { this.prescriptions = prescriptions; }
    
    
    // Méthodes métier
    public String getInfoComplete() {
        return nom + " " + dosage + unite + " - " + forme;
    }
    
    public String getDescriptionTechnique() {
        return nom + " (" + principeActif + ") - " + forme + " " + dosage + unite;
    }
    
}
