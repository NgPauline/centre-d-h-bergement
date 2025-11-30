/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "participations_activites")
public class ParticipationActivite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_id", nullable = false)
    private Resident resident;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activite_id", nullable = false)
    private Activite activite;
    
    private Boolean present = false;
    private String evaluation;
    private Integer note; // 1-10
    private String observations;
    private String comportement;
    private String progres;
    
    // Constructeurs
    public ParticipationActivite() {}
    
    public ParticipationActivite(Resident resident, Activite activite) {
        this.resident = resident;
        this.activite = activite;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Resident getResident() { return resident; }
    public void setResident(Resident resident) { this.resident = resident; }
    
    public Activite getActivite() { return activite; }
    public void setActivite(Activite activite) { this.activite = activite; }
    
    public Boolean getPresent() { return present; }
    public void setPresent(Boolean present) { this.present = present; }
    
    public String getEvaluation() { return evaluation; }
    public void setEvaluation(String evaluation) { this.evaluation = evaluation; }
    
    public Integer getNote() { return note; }
    public void setNote(Integer note) { this.note = note; }
    
    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
    
    public String getComportement() { return comportement; }
    public void setComportement(String comportement) { this.comportement = comportement; }
    
    public String getProgres() { return progres; }
    public void setProgres(String progres) { this.progres = progres; }
    
    
    // Méthodes métier
    public void enregistrerPresence(Boolean present, String observations) {
        this.present = present;
        this.observations = observations;
    }
    
    public void evaluer(Integer note, String evaluation, String progres) {
        this.note = note;
        this.evaluation = evaluation;
        this.progres = progres;
    }
    
    public boolean estPositive() {
        return note != null && note >= 7;
    }
    
    public String getStatutParticipation() {
        if (present == null) return "Inscrit";
        return present ? "Présent" : "Absent";
    }
    
}
