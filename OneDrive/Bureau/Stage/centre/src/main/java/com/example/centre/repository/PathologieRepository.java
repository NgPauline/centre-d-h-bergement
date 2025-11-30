/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.centre.repository;

import com.example.centre.entity.Pathologie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PathologieRepository extends JpaRepository<Pathologie, Long> {
    
    // Trouver toutes les pathologies d'un dossier médical
    List<Pathologie> findByDossierMedicalId(Long dossierMedicalId);
    
    // Trouver les pathologies chroniques
    List<Pathologie> findByChroniqueTrue();
    
    // Trouver les pathologies par nom
    List<Pathologie> findByNomContainingIgnoreCase(String nom);
    
    // Trouver les pathologies diagnostiquées après une certaine date
    List<Pathologie> findByDateDiagnosticAfter(LocalDate date);
    
    // Trouver les pathologies avec traitement
    @Query("SELECT p FROM Pathologie p WHERE p.traitement IS NOT NULL AND p.traitement != ''")
    List<Pathologie> findWithTraitement();
    
    // Compter le nombre de pathologies chroniques par dossier médical
    long countByDossierMedicalIdAndChroniqueTrue(Long dossierMedicalId);
    
    // Recherche avancée par multiple critères
    @Query("SELECT p FROM Pathologie p WHERE " +
           "(:nom IS NULL OR LOWER(p.nom) LIKE LOWER(CONCAT('%', :nom, '%'))) AND " +
           "(:chronique IS NULL OR p.chronique = :chronique)")
    List<Pathologie> searchByCriteria(@Param("nom") String nom, @Param("chronique") Boolean chronique);
}