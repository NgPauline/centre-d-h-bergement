/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "personnel")
public class Personnel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false)
    private String prenom;
    
    private LocalDate dateNaissance;
    private String numeroNational;
    private String telephone;
    private String email;
    
    @Embedded
    private Adresse adresse;
    
    private String matricule;
    private LocalDate dateEmbauche;
    
    @Enumerated(EnumType.STRING)
    private TypeContrat typeContrat;
    
    private Double salaire;
    
    @OneToMany(mappedBy = "personnel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Qualification> qualifications = new ArrayList<>();
    
    @OneToOne(mappedBy = "personnel", fetch = FetchType.LAZY)
    private Utilisateur utilisateur;
    
    @OneToMany(mappedBy = "personnel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Planning> plannings = new ArrayList<>();
    
    @OneToMany(mappedBy = "personnel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Conge> conges = new ArrayList<>();
    
    @Enumerated(EnumType.STRING)
    private Poste poste;
    
    // Constructeurs
    public Personnel() {}
    
    public Personnel(String nom, String prenom, String matricule, LocalDate dateEmbauche) {
        this.nom = nom;
        this.prenom = prenom;
        this.matricule = matricule;
        this.dateEmbauche = dateEmbauche;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }
    
    public String getNumeroNational() { return numeroNational; }
    public void setNumeroNational(String numeroNational) { this.numeroNational = numeroNational; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Adresse getAdresse() { return adresse; }
    public void setAdresse(Adresse adresse) { this.adresse = adresse; }
    
    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }
    
    public LocalDate getDateEmbauche() { return dateEmbauche; }
    public void setDateEmbauche(LocalDate dateEmbauche) { this.dateEmbauche = dateEmbauche; }
    
    public TypeContrat getTypeContrat() { return typeContrat; }
    public void setTypeContrat(TypeContrat typeContrat) { this.typeContrat = typeContrat; }
    
    public Double getSalaire() { return salaire; }
    public void setSalaire(Double salaire) { this.salaire = salaire; }
    
    public List<Qualification> getQualifications() { return qualifications; }
    public void setQualifications(List<Qualification> qualifications) { this.qualifications = qualifications; }
    
    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }
    
    public List<Planning> getPlannings() { return plannings; }
    public void setPlannings(List<Planning> plannings) { this.plannings = plannings; }
    
    public List<Conge> getConges() { return conges; }
    public void setConges(List<Conge> conges) { this.conges = conges; }
    
    public Poste getPoste() { return poste; }
    public void setPoste(Poste poste) { this.poste = poste; } 
    
    
    // Méthodes métier
    public boolean estDisponible() {
        return conges.stream().noneMatch(c -> 
            c.getStatut() == StatutDemande.APPROUVEE && 
            c.getDateDebut().isBefore(LocalDate.now()) && 
            c.getDateFin().isAfter(LocalDate.now())
        );
    }
    
    public int calculerAnciennete() {
        if (dateEmbauche == null) return 0;
        return Period.between(dateEmbauche, LocalDate.now()).getYears();
    }
    
    public String getNomComplet() {
        return prenom + " " + nom;
    }
    
    public int getAge() {
        if (dateNaissance == null) return 0;
        return Period.between(dateNaissance, LocalDate.now()).getYears();
    }
    
}