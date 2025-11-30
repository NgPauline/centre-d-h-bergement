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
@Table(name = "equipements")
public class Equipement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    private String description;
    private String marque;
    private String modele;
    private String numeroSerie;
    
    @Enumerated(EnumType.STRING)
    private TypeEquipement type;
    
    private LocalDate dateAcquisition;
    private LocalDate dateMaintenance;
    private LocalDate dateProchaineMaintenance;
    private Boolean enService = true;
    
    @ManyToMany(mappedBy = "equipements", fetch = FetchType.LAZY)
    private List<Chambre> chambres = new ArrayList<>();
    
    // Constructeurs
    public Equipement() {}
    
    public Equipement(String nom, TypeEquipement type) {
        this.nom = nom;
        this.type = type;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }
    
    public String getModele() { return modele; }
    public void setModele(String modele) { this.modele = modele; }
    
    public String getNumeroSerie() { return numeroSerie; }
    public void setNumeroSerie(String numeroSerie) { this.numeroSerie = numeroSerie; }
    
    public TypeEquipement getType() { return type; }
    public void setType(TypeEquipement type) { this.type = type; }
    
    public LocalDate getDateAcquisition() { return dateAcquisition; }
    public void setDateAcquisition(LocalDate dateAcquisition) { this.dateAcquisition = dateAcquisition; }
    
    public LocalDate getDateMaintenance() { return dateMaintenance; }
    public void setDateMaintenance(LocalDate dateMaintenance) { this.dateMaintenance = dateMaintenance; }
    
    public LocalDate getDateProchaineMaintenance() { return dateProchaineMaintenance; }
    public void setDateProchaineMaintenance(LocalDate dateProchaineMaintenance) { this.dateProchaineMaintenance = dateProchaineMaintenance; }
    
    public Boolean getEnService() { return enService; }
    public void setEnService(Boolean enService) { this.enService = enService; }
    
    public List<Chambre> getChambres() { return chambres; }
    public void setChambres(List<Chambre> chambres) { this.chambres = chambres; }
    
    // Méthodes métier
    public boolean verifierDisponibilite() {
        return enService && (dateMaintenance == null || 
               dateProchaineMaintenance == null || 
               LocalDate.now().isBefore(dateProchaineMaintenance));
    }
    
    public boolean necessiteMaintenance() {
        return dateProchaineMaintenance != null && 
               LocalDate.now().isAfter(dateProchaineMaintenance);
    }
    
    public void programmerMaintenance(LocalDate date) {
        this.dateMaintenance = LocalDate.now();
        this.dateProchaineMaintenance = date;
    }
    
    public String getInfoComplete() {
        return nom + " (" + type + ") - " + (enService ? "En service" : "Hors service");
    }
    
}
