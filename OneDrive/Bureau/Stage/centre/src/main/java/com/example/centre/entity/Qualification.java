/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "qualifications")
public class Qualification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String intitule;
    
    private String organisme;
    private LocalDate dateObtention;
    private LocalDate dateExpiration;
    private String numeroCertificat;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personnel_id")
    private Personnel personnel;
    
    // Constructeurs
    public Qualification() {}
    
    public Qualification(String intitule, String organisme, LocalDate dateObtention) {
        this.intitule = intitule;
        this.organisme = organisme;
        this.dateObtention = dateObtention;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getIntitule() { return intitule; }
    public void setIntitule(String intitule) { this.intitule = intitule; }
    
    public String getOrganisme() { return organisme; }
    public void setOrganisme(String organisme) { this.organisme = organisme; }
    
    public LocalDate getDateObtention() { return dateObtention; }
    public void setDateObtention(LocalDate dateObtention) { this.dateObtention = dateObtention; }
    
    public LocalDate getDateExpiration() { return dateExpiration; }
    public void setDateExpiration(LocalDate dateExpiration) { this.dateExpiration = dateExpiration; }
    
    public String getNumeroCertificat() { return numeroCertificat; }
    public void setNumeroCertificat(String numeroCertificat) { this.numeroCertificat = numeroCertificat; }
    
    public Personnel getPersonnel() { return personnel; }
    public void setPersonnel(Personnel personnel) { this.personnel = personnel; }
    
    
    // Méthodes métier
    public boolean estValide() {
        if (dateExpiration == null) return true;
        return LocalDate.now().isBefore(dateExpiration) || LocalDate.now().isEqual(dateExpiration);
    }
    
    public boolean estExpiree() {
        return dateExpiration != null && LocalDate.now().isAfter(dateExpiration);
    }
    
    public long getJoursAvantExpiration() {
        if (dateExpiration == null) return Long.MAX_VALUE;
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), dateExpiration);
    }
    
}