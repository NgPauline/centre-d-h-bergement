/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.centre.repository;

import com.example.centre.entity.Allergie;
import com.example.centre.entity.NiveauGravite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllergieRepository extends JpaRepository<Allergie, Long> {
    
    List<Allergie> findByDossierMedicalId(Long dossierMedicalId);
    
    List<Allergie> findByGravite(NiveauGravite gravite);
    
    // CORRECTION: Utilisation correcte de l'enum dans la requête
    @Query("SELECT a FROM Allergie a WHERE a.gravite = com.example.centre.entity.NiveauGravite.SEVERE OR a.gravite = com.example.centre.entity.NiveauGravite.CRITIQUE")
    List<Allergie> findAllergiesCritiques();
    
    List<Allergie> findBySubstanceContainingIgnoreCase(String substance);
    
    boolean existsByDossierMedicalIdAndSubstanceIgnoreCase(Long dossierMedicalId, String substance);
    
    long countByDossierMedicalId(Long dossierMedicalId);
    
    @Query("SELECT a FROM Allergie a WHERE a.traitement IS NOT NULL AND a.traitement != ''")
    List<Allergie> findWithTraitement();
}