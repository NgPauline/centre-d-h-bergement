/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "contrats")
public class Contrat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_id", nullable = false)
    private Resident resident;
    
    @Column(nullable = false)
    private LocalDate dateDebut;
    
    private LocalDate dateFin;
    private Double tarifMensuel;
    private String conditions;
    private Boolean actif = true;
    
    @Lob
    private byte[] documentPDF;
    
    private String numeroContrat;
    private String typeHebergement;
    private String dureeContrat;
    private String conditionsParticulieres;
    
    // Constructeurs
    public Contrat() {}
    
    public Contrat(Resident resident, LocalDate dateDebut, Double tarifMensuel) {
        this.resident = resident;
        this.dateDebut = dateDebut;
        this.tarifMensuel = tarifMensuel;
        this.numeroContrat = "CTR-" + System.currentTimeMillis();
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Resident getResident() { return resident; }
    public void setResident(Resident resident) { this.resident = resident; }
    
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    
    public Double getTarifMensuel() { return tarifMensuel; }
    public void setTarifMensuel(Double tarifMensuel) { this.tarifMensuel = tarifMensuel; }
    
    public String getConditions() { return conditions; }
    public void setConditions(String conditions) { this.conditions = conditions; }
    
    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }
    
    public byte[] getDocumentPDF() { return documentPDF; }
    public void setDocumentPDF(byte[] documentPDF) { this.documentPDF = documentPDF; }
    
    public String getNumeroContrat() { return numeroContrat; }
    public void setNumeroContrat(String numeroContrat) { this.numeroContrat = numeroContrat; }
    
    public String getTypeHebergement() { return typeHebergement; }
    public void setTypeHebergement(String typeHebergement) { this.typeHebergement = typeHebergement; }
    
    public String getDureeContrat() { return dureeContrat; }
    public void setDureeContrat(String dureeContrat) { this.dureeContrat = dureeContrat; }
    
    public String getConditionsParticulieres() { return conditionsParticulieres; }
    public void setConditionsParticulieres(String conditionsParticulieres) { this.conditionsParticulieres = conditionsParticulieres; }

// Méthodes métier
    public void renouveler(LocalDate nouvelleDateFin, Double nouveauTarif) {
        this.dateFin = nouvelleDateFin;
        if (nouveauTarif != null) {
            this.tarifMensuel = nouveauTarif;
        }
        this.actif = true;
    }
    
    public void resilier(String motif) {
        this.actif = false;
        this.dateFin = LocalDate.now();
        this.conditionsParticulieres = (conditionsParticulieres != null ? conditionsParticulieres + " | " : "") + 
                                      "Résiliation: " + motif;
    }
    
    public boolean estEnCours() {
        LocalDate aujourdhui = LocalDate.now();
        return actif && !aujourdhui.isBefore(dateDebut) && 
               (dateFin == null || !aujourdhui.isAfter(dateFin));
    }
    
    public boolean estExpire() {
        return dateFin != null && LocalDate.now().isAfter(dateFin);
    }
    
    public long getJoursRestants() {
        if (dateFin == null) return Long.MAX_VALUE;
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), dateFin);
    }
    
    public String getStatutContrat() {
        if (!actif) return "Résilié";
        if (estExpire()) return "Expiré";
        if (estEnCours()) return "En cours";
        return "En attente";
    }
    
}