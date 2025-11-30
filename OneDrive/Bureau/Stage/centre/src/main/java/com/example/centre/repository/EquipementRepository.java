/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.centre.repository;

import com.example.centre.entity.Equipement;
import com.example.centre.entity.TypeEquipement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EquipementRepository extends JpaRepository<Equipement, Long> {
    
    // Trouver un équipement par son nom
    Optional<Equipement> findByNom(String nom);
    
    // Trouver un équipement par son numéro de série
    Optional<Equipement> findByNumeroSerie(String numeroSerie);
    
    // Trouver les équipements par type
    List<Equipement> findByType(TypeEquipement type);
    
    // Trouver les équipements par marque
    List<Equipement> findByMarque(String marque);
    
    // Trouver les équipements en service
    List<Equipement> findByEnServiceTrue();
    
    // Trouver les équipements hors service
    List<Equipement> findByEnServiceFalse();
    
    // Trouver les équipements nécessitant une maintenance (date dépassée)
    List<Equipement> findByDateProchaineMaintenanceBefore(LocalDate date);
    
    // Trouver les équipements nécessitant une maintenance bientôt (dans les 30 jours)
    @Query("SELECT e FROM Equipement e WHERE e.dateProchaineMaintenance BETWEEN :startDate AND :endDate")
    List<Equipement> findEquipementsAvecMaintenanceProche(@Param("startDate") LocalDate startDate, 
                                                         @Param("endDate") LocalDate endDate);
    
    // Compter le nombre d'équipements par type
    @Query("SELECT e.type, COUNT(e) FROM Equipement e GROUP BY e.type")
    List<Object[]> countByType();
    
    // Trouver les équipements acquis après une certaine date
    List<Equipement> findByDateAcquisitionAfter(LocalDate date);
    
    // Trouver les équipements par modèle et marque
    List<Equipement> findByMarqueAndModele(String marque, String modele);
    
    // Vérifier si un équipement avec ce numéro de série existe déjà (pour l'unicité)
    boolean existsByNumeroSerie(String numeroSerie);
    
    // Trouver les équipements avec maintenance prévue mais pas encore effectuée
    @Query("SELECT e FROM Equipement e WHERE e.dateProchaineMaintenance IS NOT NULL AND e.dateMaintenance IS NULL")
    List<Equipement> findEquipementsAvecMaintenancePlanifieeNonEffectuee();
    
    // Statistiques des équipements par statut de maintenance
    @Query("SELECT " +
           "SUM(CASE WHEN e.dateProchaineMaintenance < CURRENT_DATE THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN e.dateProchaineMaintenance IS NULL THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN e.dateProchaineMaintenance >= CURRENT_DATE THEN 1 ELSE 0 END) " +
           "FROM Equipement e WHERE e.enService = true")
    Object[] getStatistiquesMaintenance();
}
