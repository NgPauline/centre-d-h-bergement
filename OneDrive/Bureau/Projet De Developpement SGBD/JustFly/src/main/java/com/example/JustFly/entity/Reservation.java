/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.JustFly.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-incrémenté
    private Long id;

    @Column(name = "date_reservation", nullable = false)
    private LocalDate dateReservation;

    @Column(name = "classe", nullable = false)
    private String classe;

    @ManyToOne
    @JoinColumn(name = "vol_id", nullable = false)  // Clé étrangère vers Vol
    private Vol vol;

    @ManyToOne
    @JoinColumn(name = "passager_id", nullable = false)  // Clé étrangère vers Passager
    private Passager passager;

    // Constructeurs
    public Reservation() {}

    public Reservation(LocalDate dateReservation, String classe, Vol vol, Passager passager) {
        this.dateReservation = dateReservation;
        this.classe = classe;
        this.vol = vol;
        this.passager = passager;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDate dateReservation) {
        this.dateReservation = dateReservation;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public Vol getVol() {
        return vol;
    }

    public void setVol(Vol vol) {
        this.vol = vol;
    }

    public Passager getPassager() {
        return passager;
    }

    public void setPassager(Passager passager) {
        this.passager = passager;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", dateReservation=" + dateReservation +
                ", classe='" + classe + '\'' +
                ", vol=" + vol.getNumVol() +
                ", passager=" + passager.getNom() +
                '}';
    }
}
