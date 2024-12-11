/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.JustFly.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "aeroports")
public class Aeroport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-incrémenté
    private Long id;

    @Column(name = "nom_aeroport", nullable = false, unique = true)
    private String nomAeroport;

    @Column(name = "ville", nullable = false)
    private String ville;

    @Column(name = "pays", nullable = false)
    private String pays;

    // Constructeurs
    public Aeroport() {}

    public Aeroport(String nomAeroport, String ville, String pays) {
        this.nomAeroport = nomAeroport;
        this.ville = ville;
        this.pays = pays;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomAeroport() {
        return nomAeroport;
    }

    public void setNomAeroport(String nomAeroport) {
        this.nomAeroport = nomAeroport;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    @Override
    public String toString() {
        return "Aeroport{" +
                "id=" + id +
                ", nomAeroport='" + nomAeroport + '\'' +
                ", ville='" + ville + '\'' +
                ", pays='" + pays + '\'' +
                '}';
    }
}
