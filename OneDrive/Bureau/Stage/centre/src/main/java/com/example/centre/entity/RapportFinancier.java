/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "rapports_financiers")
public class RapportFinancier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String titre;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeRapport type;
    
    @Column(nullable = false)
    private LocalDate dateDebut;
    
    @Column(nullable = false)
    private LocalDate dateFin;
    
    @Column(nullable = false)
    private LocalDate dateGeneration;
    
    private String resume;
    private String conclusions;
    private String recommendations;
    
    @Lob
    private byte[] documentPDF;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comptable_id")
    private Personnel comptable;
    
    // Constructeurs
    public RapportFinancier() {
        this.dateGeneration = LocalDate.now();
    }
    
    public RapportFinancier(String titre, TypeRapport type, LocalDate dateDebut, LocalDate dateFin) {
        this();
        this.titre = titre;
        this.type = type;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    
    public TypeRapport getType() { return type; }
    public void setType(TypeRapport type) { this.type = type; }
    
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    
    public LocalDate getDateGeneration() { return dateGeneration; }
    public void setDateGeneration(LocalDate dateGeneration) { this.dateGeneration = dateGeneration; }
    
    public String getResume() { return resume; }
    public void setResume(String resume) { this.resume = resume; }
    
    public String getConclusions() { return conclusions; }
    public void setConclusions(String conclusions) { this.conclusions = conclusions; }
    
    public String getRecommendations() { return recommendations; }
    public void setRecommendations(String recommendations) { this.recommendations = recommendations; }
    
    public byte[] getDocumentPDF() { return documentPDF; }
    public void setDocumentPDF(byte[] documentPDF) { this.documentPDF = documentPDF; }
    
    public Personnel getComptable() { return comptable; }
    public void setComptable(Personnel comptable) { this.comptable = comptable; }
    
    
    // Méthodes métier
    public void generer() {
        // Logique de génération du rapport
        this.resume = "Rapport " + type + " généré le " + dateGeneration;
        this.documentPDF = new byte[0]; // Simulation PDF
    }
    
    public String exporter() {
        // Logique d'exportation
        return "Rapport_" + titre.replace(" ", "_") + "_" + dateGeneration + ".pdf";
    }
    
    public boolean estRapportMensuel() {
        return type == TypeRapport.MENSUEL;
    }
    
    public boolean estRapportAnnuel() {
        return type == TypeRapport.ANNUEL;
    }
    
    public boolean estBilan() {
        return type == TypeRapport.BILAN;
    }
    
    public long getDureePeriode() {
        return java.time.temporal.ChronoUnit.DAYS.between(dateDebut, dateFin);
    }
    
    public String getPeriode() {
        return dateDebut + " au " + dateFin;
    }
    
}