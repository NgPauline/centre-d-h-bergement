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
@Table(name = "programmes_personnalises")
public class ProgrammePersonnalise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_id", nullable = false)
    private Resident resident;
    
    @Column(nullable = false)
    private String titre;
    
    private String objectifs;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String methodologie;
    private String criteresSucces;
    private String observations;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id")
    private Personnel responsable;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "programme_activites",
        joinColumns = @JoinColumn(name = "programme_id"),
        inverseJoinColumns = @JoinColumn(name = "activite_id")
    )
    private List<Activite> activites = new ArrayList<>();
    
    // Constructeurs
    public ProgrammePersonnalise() {}
    
    public ProgrammePersonnalise(Resident resident, String titre, LocalDate dateDebut) {
        this.resident = resident;
        this.titre = titre;
        this.dateDebut = dateDebut;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Resident getResident() { return resident; }
    public void setResident(Resident resident) { this.resident = resident; }
    
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    
    public String getObjectifs() { return objectifs; }
    public void setObjectifs(String objectifs) { this.objectifs = objectifs; }
    
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    
    public String getMethodologie() { return methodologie; }
    public void setMethodologie(String methodologie) { this.methodologie = methodologie; }
    
    public String getCriteresSucces() { return criteresSucces; }
    public void setCriteresSucces(String criteresSucces) { this.criteresSucces = criteresSucces; }
    
    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
    
    public Personnel getResponsable() { return responsable; }
    public void setResponsable(Personnel responsable) { this.responsable = responsable; }
    
    public List<Activite> getActivites() { return activites; }
    public void setActivites(List<Activite> activites) { this.activites = activites; }
    
    
    // Méthodes métier
    public void ajouterActivite(Activite activite) {
        if (!activites.contains(activite)) {
            activites.add(activite);
            activite.getProgrammes().add(this);
        }
    }
    
    public void retirerActivite(Activite activite) {
        activites.remove(activite);
        activite.getProgrammes().remove(this);
    }
    
    public void evaluerProgression(String evaluation, Integer pourcentageCompletion) {
        this.observations = evaluation;
        // Logique d'évaluation de la progression
    }
    
    public boolean estEnCours() {
        LocalDate aujourdhui = LocalDate.now();
        return !aujourdhui.isBefore(dateDebut) && (dateFin == null || !aujourdhui.isAfter(dateFin));
    }
    
    public boolean estTermine() {
        return dateFin != null && LocalDate.now().isAfter(dateFin);
    }
    
    public long getJoursRestants() {
        if (dateFin == null) return Long.MAX_VALUE;
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), dateFin);
    }
    
    public int getNombreActivites() {
        return activites.size();
    }
    
}
