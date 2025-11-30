/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chambres")
public class Chambre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String numero;
    
    private Integer etage;
    
    @Column(nullable = false)
    private Integer capacite;
    
    @Enumerated(EnumType.STRING)
    private TypeChambre type;
    
    
    @Column(name = "est_accessible", nullable = false)
    private Boolean accessible = true;
    
    @Column(length = 500)
    private String description;
    
    @OneToMany(mappedBy = "chambre", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Resident> residents = new ArrayList<>();
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "chambre_equipements",
        joinColumns = @JoinColumn(name = "chambre_id"),
        inverseJoinColumns = @JoinColumn(name = "equipement_id")
    )
    private List<Equipement> equipements = new ArrayList<>();
    
    @OneToMany(mappedBy = "chambre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HistoriqueChambre> historique = new ArrayList<>();
    
    // Constructeurs
    public Chambre() {}
    
    public Chambre(String numero, Integer etage, Integer capacite, TypeChambre type) {
        this.numero = numero;
        this.etage = etage;
        this.capacite = capacite;
        this.type = type;
        this.accessible = true;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public Integer getEtage() { return etage; }
    public void setEtage(Integer etage) { this.etage = etage; }
    
    public Integer getCapacite() { return capacite; }
    public void setCapacite(Integer capacite) { this.capacite = capacite; }
    
    public TypeChambre getType() { return type; }
    public void setType(TypeChambre type) { this.type = type; }
    
    public Boolean getAccessible() { return accessible; }
    public void setAccessible(Boolean accessible) { this.accessible = accessible; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public List<Resident> getResidents() { return residents; }
    public void setResidents(List<Resident> residents) { this.residents = residents; }
    
    public List<Equipement> getEquipements() { return equipements; }
    public void setEquipements(List<Equipement> equipements) { this.equipements = equipements; }
    
    public List<HistoriqueChambre> getHistorique() { return historique; }
    public void setHistorique(List<HistoriqueChambre> historique) { this.historique = historique; }
    
    // Méthodes métier
    public boolean estDisponible() {
        return residents.size() < capacite && accessible;
    }
    
    public int getNombrePlacesLibres() {
        return capacite - residents.size();
    }
    
    public boolean estPleine() {
        return residents.size() >= capacite;
    }
    
    public void ajouterResident(Resident resident) {
        if (estDisponible()) {
            residents.add(resident);
            resident.setChambre(this);
        }
    }
    
    public void retirerResident(Resident resident) {
        residents.remove(resident);
        resident.setChambre(null);
    }
    
    public boolean contientEquipement(TypeEquipement typeEquipement) {
        return equipements.stream()
                .anyMatch(e -> e.getType() == typeEquipement);
    }
    
}