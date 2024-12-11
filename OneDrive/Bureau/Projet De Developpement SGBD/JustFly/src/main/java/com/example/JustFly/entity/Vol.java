/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.JustFly.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Entity
@Table(name = "vols")
public class Vol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-incrémenté
    private Long id;

    @Column(name = "num_vol", nullable = false)
    private String numVol;

    @Column(name = "date_depart", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateDepart;

    @Column(name = "date_arrivee", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateArrivee;

    @Column(name = "passagers_max", nullable = false)
    private int passagersMax;

    @Column(name = "aeroport_depart", nullable = false)
    private String aeroportDepart;

    @Column(name = "aeroport_arrivee", nullable = false)
    private String aeroportArrivee;

    @ManyToOne
    @JoinColumn(name = "id_compagnie", nullable = false)  // Clé étrangère vers CompagnieAerienne
    private CompagnieAerienne compagnie;

    // Constructeurs
    public Vol() {}

    public Vol(String numVol, LocalDateTime dateDepart, LocalDateTime dateArrivee, int passagersMax, String aeroportDepart, String aeroportArrivee, CompagnieAerienne compagnie) {
        this.numVol = numVol;
        this.dateDepart = dateDepart;
        this.dateArrivee = dateArrivee;
        this.passagersMax = passagersMax;
        this.aeroportDepart = aeroportDepart;
        this.aeroportArrivee = aeroportArrivee;
        this.compagnie = compagnie;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumVol() {
        return numVol;
    }

    public void setNumVol(String numVol) {
        this.numVol = numVol;
    }

    public LocalDateTime getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(LocalDateTime dateDepart) {
        this.dateDepart = dateDepart;
    }

    public LocalDateTime getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(LocalDateTime dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public int getPassagersMax() {
        return passagersMax;
    }

    public void setPassagersMax(int passagersMax) {
        this.passagersMax = passagersMax;
    }

    public String getAeroportDepart() {
        return aeroportDepart;
    }

    public void setAeroportDepart(String aeroportDepart) {
        this.aeroportDepart = aeroportDepart;
    }

    public String getAeroportArrivee() {
        return aeroportArrivee;
    }

    public void setAeroportArrivee(String aeroportArrivee) {
        this.aeroportArrivee = aeroportArrivee;
    }

    public CompagnieAerienne getCompagnie() {
        return compagnie;
    }

    public void setCompagnie(CompagnieAerienne compagnie) {
        this.compagnie = compagnie;
    }

    @Override
    public String toString() {
        return "Vol{" +
                "id=" + id +
                ", numVol='" + numVol + '\'' +
                ", dateDepart=" + dateDepart +
                ", dateArrivee=" + dateArrivee +
                ", passagersMax=" + passagersMax +
                ", aeroportDepart='" + aeroportDepart + '\'' +
                ", aeroportArrivee='" + aeroportArrivee + '\'' +
                ", compagnie=" + compagnie.getNomCompagnie() +
                '}';
    }
}
