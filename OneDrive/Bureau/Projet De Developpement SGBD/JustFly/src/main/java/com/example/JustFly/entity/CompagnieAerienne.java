/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.JustFly.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "compagnies_aeriennes")
public class CompagnieAerienne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-incrémenté
    private Long id;

    @Column(name = "nom_compagnie", nullable = false, unique = true)
    private String nomCompagnie;

    @OneToMany(mappedBy = "compagnie", cascade = CascadeType.ALL)
    private List<Vol> vols;  // Liste des vols pour une compagnie

    // Constructeurs
    public CompagnieAerienne() {}

    public CompagnieAerienne(String nomCompagnie) {
        this.nomCompagnie = nomCompagnie;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomCompagnie() {
        return nomCompagnie;
    }

    public void setNomCompagnie(String nomCompagnie) {
        this.nomCompagnie = nomCompagnie;
    }

    public List<Vol> getVols() {
        return vols;
    }

    public void setVols(List<Vol> vols) {
        this.vols = vols;
    }

    @Override
    public String toString() {
        return "CompagnieAerienne{" +
                "id=" + id +
                ", nomCompagnie='" + nomCompagnie + '\'' +
                '}';
    }
}
