/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "activites")
public class Activite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    private String description;
    
    @Enumerated(EnumType.STRING)
    private TypeActivite type;
    
    @Column(nullable = false)
    private LocalDateTime dateHeure;
    
    private Integer duree; // en minutes
    private Integer capaciteMax;
    private String lieu;
    private String materielNecessaire;
    private String objectifs;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id")
    private Personnel responsable;
    
    @OneToMany(mappedBy = "activite", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ParticipationActivite> participations = new ArrayList<>();
    
    @ManyToMany(mappedBy = "activites", fetch = FetchType.LAZY)
    private List<ProgrammePersonnalise> programmes = new ArrayList<>();
    
    // Constructeurs
    public Activite() {}
    
    public Activite(String nom, TypeActivite type, LocalDateTime dateHeure, Integer duree) {
        this.nom = nom;
        this.type = type;
        this.dateHeure = dateHeure;
        this.duree = duree;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public TypeActivite getType() { return type; }
    public void setType(TypeActivite type) { this.type = type; }
    
    public LocalDateTime getDateHeure() { return dateHeure; }
    public void setDateHeure(LocalDateTime dateHeure) { this.dateHeure = dateHeure; }
    
    public Integer getDuree() { return duree; }
    public void setDuree(Integer duree) { this.duree = duree; }
    
    public Integer getCapaciteMax() { return capaciteMax; }
    public void setCapaciteMax(Integer capaciteMax) { this.capaciteMax = capaciteMax; }
    
    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }
    
    public String getMaterielNecessaire() { return materielNecessaire; }
    public void setMaterielNecessaire(String materielNecessaire) { this.materielNecessaire = materielNecessaire; }
    
    public String getObjectifs() { return objectifs; }
    public void setObjectifs(String objectifs) { this.objectifs = objectifs; }
    
    public Personnel getResponsable() { return responsable; }
    public void setResponsable(Personnel responsable) { this.responsable = responsable; }
    
    public List<ParticipationActivite> getParticipations() { return participations; }
    public void setParticipations(List<ParticipationActivite> participations) { this.participations = participations; }
    
    public List<ProgrammePersonnalise> getProgrammes() { return programmes; }
    public void setProgrammes(List<ProgrammePersonnalise> programmes) { this.programmes = programmes; }
    
    // Méthodes métier
    public void inscrireResident(Resident resident) {
        if (!estComplete()) {
            ParticipationActivite participation = new ParticipationActivite();
            participation.setResident(resident);
            participation.setActivite(this);
            participation.setPresent(false);
            participations.add(participation);
        }
    }
    
    public void desinscrireResident(Resident resident) {
        participations.removeIf(p -> p.getResident().equals(resident));
    }
    
    public void annulerActivite() {
        // Marquer toutes les participations comme annulées
        participations.forEach(p -> p.setPresent(false));
    }
    
    public boolean estComplete() {
        return participations.size() >= capaciteMax;
    }
    
    public int getNombreInscrits() {
        return participations.size();
    }
    
    public int getPlacesRestantes() {
        return capaciteMax - participations.size();
    }
    
    public boolean estPassee() {
        return LocalDateTime.now().isAfter(dateHeure);
    }
    
    public boolean estEnCours() {
        LocalDateTime fin = dateHeure.plusMinutes(duree);
        return LocalDateTime.now().isAfter(dateHeure) && LocalDateTime.now().isBefore(fin);
    }
    
    public long getJoursAvantActivite() {
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDateTime.now().toLocalDate(), dateHeure.toLocalDate());
    }
    
}