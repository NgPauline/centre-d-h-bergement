/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "notes_quotidiennes")
public class NoteQuotidienne {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_id", nullable = false)
    private Resident resident;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auteur_id", nullable = false)
    private Personnel auteur;
    
    @Column(nullable = false)
    private LocalDateTime dateHeure;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenu;
    
    @Enumerated(EnumType.STRING)
    private Humeur humeur;
    
    private Boolean important = false;
    private String typeNote; // Médicale, Éducative, Comportementale, etc.
    private String actions;
    
    // Constructeurs
    public NoteQuotidienne() {
        this.dateHeure = LocalDateTime.now();
    }
    
    public NoteQuotidienne(Resident resident, Personnel auteur, String contenu) {
        this();
        this.resident = resident;
        this.auteur = auteur;
        this.contenu = contenu;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Resident getResident() { return resident; }
    public void setResident(Resident resident) { this.resident = resident; }
    
    public Personnel getAuteur() { return auteur; }
    public void setAuteur(Personnel auteur) { this.auteur = auteur; }
    
    public LocalDateTime getDateHeure() { return dateHeure; }
    public void setDateHeure(LocalDateTime dateHeure) { this.dateHeure = dateHeure; }
    
    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }
    
    public Humeur getHumeur() { return humeur; }
    public void setHumeur(Humeur humeur) { this.humeur = humeur; }
    
    public Boolean getImportant() { return important; }
    public void setImportant(Boolean important) { this.important = important; }
    
    public String getTypeNote() { return typeNote; }
    public void setTypeNote(String typeNote) { this.typeNote = typeNote; }
    
    public String getActions() { return actions; }
    public void setActions(String actions) { this.actions = actions; }
    
    // Méthodes métier
    public void marquerImportant() {
        this.important = true;
    }
    
    public void marquerNormal() {
        this.important = false;
    }
    
    public boolean estRecent() {
        return dateHeure.isAfter(LocalDateTime.now().minusDays(1));
    }
    
    public boolean estDeLaJournee() {
        return dateHeure.toLocalDate().equals(LocalDate.now());
    }
    
    public String getResume() {
        if (contenu.length() <= 100) return contenu;
        return contenu.substring(0, 100) + "...";
    }
    
    public boolean estNoteMedicale() {
        return "Médicale".equals(typeNote);
    }
    
    public boolean estNoteEducative() {
        return "Éducative".equals(typeNote);
    }
    
}
