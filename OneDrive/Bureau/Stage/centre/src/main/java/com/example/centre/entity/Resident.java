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
@Table(name = "residents")
public class Resident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false)
    private String prenom;
    
    @Column(nullable = false)
    private LocalDate dateNaissance;
    
    @Column(nullable = false)
    private String numeroNational;
    
    private String telephone;
    
    private String email;
    
    @Lob
    private byte[] photo;
    
    @Enumerated(EnumType.STRING)
    private TypeHandicap typeHandicap;
    
    private Integer degreHandicap; // 1-10 ou selon échelle définie
    
    @Enumerated(EnumType.STRING)
    private StatutResident statut = StatutResident.ACTIF;
    
    private LocalDate dateAdmission;
    private LocalDate dateSortie;
    private String observations;
    private Boolean besoinInfirmier = false;
    private Boolean besoinAideSoignant = false;
    
    @Embedded
    private Adresse adresse;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chambre_id", nullable = true, foreignKey = @ForeignKey(name = "FK_resident_chambre"))
    private Chambre chambre; 
    
    @OneToMany(mappedBy = "resident", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ContactUrgence> contactsUrgence = new ArrayList<>();
    
    @OneToOne(mappedBy = "resident", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DossierMedical dossierMedical;
    
    @OneToMany(mappedBy = "resident", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NoteQuotidienne> notesQuotidiennes = new ArrayList<>();
    
    @OneToMany(mappedBy = "resident", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RapportIncident> rapportsIncident = new ArrayList<>();
    
    @OneToMany(mappedBy = "resident", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AppelUrgence> appelsUrgence = new ArrayList<>();
    
    @OneToMany(mappedBy = "resident", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HistoriqueChambre> historiqueChambres = new ArrayList<>();
    
    @OneToMany(mappedBy = "resident", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PlanningVisite> planningVisites = new ArrayList<>();
    
    @OneToMany(mappedBy = "resident", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ParticipationActivite> participations = new ArrayList<>();
    
    @OneToOne(mappedBy = "resident", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Contrat contrat;
    
    // Constructeurs
    public Resident() {}
    
    public Resident(String nom, String prenom, LocalDate dateAdmission) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateAdmission = dateAdmission;
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
    
    public byte[] getPhoto() { return photo; }
    public void setPhoto(byte[] photo) { this.photo = photo; }
    
    public TypeHandicap getTypeHandicap() { return typeHandicap; }
    public void setTypeHandicap(TypeHandicap typeHandicap) { this.typeHandicap = typeHandicap; }
    
    public Integer getDegreHandicap() { return degreHandicap; }
    public void setDegreHandicap(Integer degreHandicap) { this.degreHandicap = degreHandicap; }
    
    public StatutResident getStatut() { return statut; }
    public void setStatut(StatutResident statut) { this.statut = statut; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public LocalDate getDateAdmission() { return dateAdmission; }
    public void setDateAdmission(LocalDate dateAdmission) { this.dateAdmission = dateAdmission; }
    
    public LocalDate getDateSortie() { return dateSortie; }
    public void setDateSortie(LocalDate dateSortie) { this.dateSortie = dateSortie; }
    
    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
    
    public Boolean getBesoinInfirmier() { return besoinInfirmier; }
    public void setBesoinInfirmier(Boolean besoinInfirmier) { this.besoinInfirmier = besoinInfirmier; }
    
    public Boolean getBesoinAideSoignant() { return besoinAideSoignant; }
    public void setBesoinAideSoignant(Boolean besoinAideSoignant) { this.besoinAideSoignant = besoinAideSoignant; }
    
    public Adresse getAdresse() { return adresse; }
    public void setAdresse(Adresse adresse) { this.adresse = adresse; }
    
    public Chambre getChambre() { return chambre; }
    public void setChambre(Chambre chambre) { this.chambre = chambre; }
    
    public List<ContactUrgence> getContactsUrgence() { return contactsUrgence; }
    public void setContactsUrgence(List<ContactUrgence> contactsUrgence) { this.contactsUrgence = contactsUrgence; }
    
    public DossierMedical getDossierMedical() { return dossierMedical; }
    public void setDossierMedical(DossierMedical dossierMedical) { this.dossierMedical = dossierMedical; }
    
    public List<NoteQuotidienne> getNotesQuotidiennes() { return notesQuotidiennes; }
    public void setNotesQuotidiennes(List<NoteQuotidienne> notesQuotidiennes) { this.notesQuotidiennes = notesQuotidiennes; }
    
    public List<RapportIncident> getRapportsIncident() { return rapportsIncident; }
    public void setRapportsIncident(List<RapportIncident> rapportsIncident) { this.rapportsIncident = rapportsIncident; }
    
    public List<AppelUrgence> getAppelsUrgence() { return appelsUrgence; }
    public void setAppelsUrgence(List<AppelUrgence> appelsUrgence) { this.appelsUrgence = appelsUrgence; }
    
    public List<HistoriqueChambre> getHistoriqueChambres() { return historiqueChambres; }
    public void setHistoriqueChambres(List<HistoriqueChambre> historiqueChambres) { this.historiqueChambres = historiqueChambres; }
    
    public List<PlanningVisite> getPlanningVisites() { return planningVisites; }
    public void setPlanningVisites(List<PlanningVisite> planningVisites) { this.planningVisites = planningVisites; }
    
    public List<ParticipationActivite> getParticipations() { return participations; }
    public void setParticipations(List<ParticipationActivite> participations) { this.participations = participations; }
    
    public Contrat getContrat() { return contrat; }
    public void setContrat(Contrat contrat) { this.contrat = contrat; }
    
    
    // Méthodes métier
    public boolean isActif() {
        return statut == StatutResident.ACTIF;
    }
    
    public long calculerDureeHebergement() {
        LocalDate start = dateAdmission != null ? dateAdmission : LocalDate.now();
        LocalDate end = dateSortie != null ? dateSortie : LocalDate.now();
        return Period.between(start, end).getDays();
    }
    
    public int getAge() {
        if (dateNaissance == null) return 0;
        return Period.between(dateNaissance, LocalDate.now()).getYears();
    }
    
    public String getNomComplet() {
        return prenom + " " + nom;
    }
    
    public void changerChambre(Chambre nouvelleChambre) {
        if (this.chambre != null) {
            this.chambre.getResidents().remove(this);
        }
        this.chambre = nouvelleChambre;
        if (nouvelleChambre != null) {
            nouvelleChambre.getResidents().add(this);
        }
    }
    
    public void sortirResident() {
        this.statut = StatutResident.SORTI;
        this.dateSortie = LocalDate.now();
        if (this.chambre != null) {
            this.chambre.getResidents().remove(this);
            this.chambre = null;
        }
    }
    
}
