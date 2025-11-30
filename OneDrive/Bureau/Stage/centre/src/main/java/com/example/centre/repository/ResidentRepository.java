/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.centre.repository;

import com.example.centre.entity.Resident;
import com.example.centre.entity.StatutResident;
import com.example.centre.entity.TypeHandicap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, Long> {
    
    // CORRECTION : Utiliser chambre.id au lieu de chambreId
    @Query("SELECT r FROM Resident r WHERE r.chambre.id = :chambreId")
    List<Resident> findByChambreId(@Param("chambreId") Long chambreId);
    
    // CORRECTION : Utiliser chambre.numero au lieu de numeroChambre
    @Query("SELECT r FROM Resident r WHERE r.chambre.numero = :numeroChambre")
    List<Resident> findByNumeroChambre(@Param("numeroChambre") String numeroChambre);
    
    List<Resident> findByNumeroNationalContainingIgnoreCase(String numeroNational);
    
    // AJOUT : Résidents avec besoin aide-soignant
    List<Resident> findByBesoinAideSoignantTrue();
    
    // AJOUT : Résidents avec besoin infirmier et statut spécifique
    List<Resident> findByBesoinInfirmierTrueAndStatut(StatutResident statut);
    
    // AJOUT : Résidents avec besoin aide-soignant et statut spécifique
    List<Resident> findByBesoinAideSoignantTrueAndStatut(StatutResident statut);
    
    // AJOUT : Résidents avec les deux besoins
    @Query("SELECT r FROM Resident r WHERE r.besoinInfirmier = true AND r.besoinAideSoignant = true AND r.statut = 'ACTIF'")
    List<Resident> findResidentsAvecLesDeuxBesoins();
    
    // Méthodes de base
    long countByStatut(StatutResident statut);
    List<Resident> findTop5ByOrderByDateAdmissionDesc();
    List<Resident> findByNomContainingIgnoreCase(String nom);
    List<Resident> findByPrenomContainingIgnoreCase(String prenom);
    Optional<Resident> findByNumeroNational(String numeroNational);
    List<Resident> findByStatut(StatutResident statut);
    List<Resident> findByStatutIn(List<StatutResident> statuts);
    List<Resident> findByTypeHandicap(TypeHandicap typeHandicap);
    List<Resident> findByDegreHandicapGreaterThanEqual(Integer degreMin);
    List<Resident> findByDateAdmissionAfter(LocalDate date);
    List<Resident> findByDateAdmissionBetween(LocalDate start, LocalDate end);
    List<Resident> findByDateSortieIsNull();
    
    // Recherche combinée
    @Query("SELECT r FROM Resident r WHERE " +
           "LOWER(r.nom) LIKE LOWER(CONCAT('%', :recherche, '%')) OR " +
           "LOWER(r.prenom) LIKE LOWER(CONCAT('%', :recherche, '%')) OR " +
           "LOWER(r.numeroNational) LIKE LOWER(CONCAT('%', :recherche, '%'))")
    List<Resident> rechercherParNomPrenomNumero(@Param("recherche") String recherche);
    
    // Statistiques
    @Query("SELECT COUNT(r) FROM Resident r WHERE r.statut = 'ACTIF'")
    long countResidentsActifs();
    
    @Query("SELECT COUNT(r) FROM Resident r WHERE r.dateSortie IS NULL")
    long countResidentsPresents();
    
    @Query("SELECT r.typeHandicap, COUNT(r) FROM Resident r GROUP BY r.typeHandicap")
    List<Object[]> countByTypeHandicap();
    
    // Recherche avancée - CORRIGÉE (4 paramètres seulement)
    @Query("SELECT r FROM Resident r WHERE " +
           "(:nom IS NULL OR LOWER(r.nom) LIKE LOWER(CONCAT('%', :nom, '%'))) AND " +
           "(:prenom IS NULL OR LOWER(r.prenom) LIKE LOWER(CONCAT('%', :prenom, '%'))) AND " +
           "(:typeHandicap IS NULL OR r.typeHandicap = :typeHandicap) AND " +
           "(:statut IS NULL OR r.statut = :statut)")
    List<Resident> rechercherAvance(
        @Param("nom") String nom,
        @Param("prenom") String prenom,
        @Param("typeHandicap") TypeHandicap typeHandicap,
        @Param("statut") StatutResident statut);
    
    // Résidents sans chambre
       @Query("SELECT r FROM Resident r WHERE r.chambre IS NULL AND r.statut = 'ACTIF'")
       List<Resident> findResidentsSansChambre();
    
    List<Resident> findByChambreIsNull();  // Version sans filtre de statut
    
    // Résidents par étage
    @Query("SELECT r FROM Resident r WHERE r.chambre.etage = :etage AND r.statut = 'ACTIF'")
    List<Resident> findByEtageChambre(@Param("etage") Integer etage);
    
    // Méthodes de comptage pour le dashboard
    long countByBesoinInfirmier(boolean besoinInfirmier);
    long countByBesoinAideSoignant(boolean besoinAideSoignant);

    // Méthode pour compter les chambres disponibles
    @Query("SELECT COUNT(c) FROM Chambre c WHERE c.residents IS EMPTY OR SIZE(c.residents) < c.capacite")
    long countChambresDisponibles();

    // Méthode pour les nouveaux résidents du mois
    @Query("SELECT COUNT(r) FROM Resident r WHERE MONTH(r.dateAdmission) = MONTH(CURRENT_DATE) AND YEAR(r.dateAdmission) = YEAR(CURRENT_DATE)")
    long countNewResidentsThisMonth();
    
 
}