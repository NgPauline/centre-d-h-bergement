/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.repository;

import com.example.centre.entity.Chambre;
import com.example.centre.entity.TypeChambre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChambreRepository extends JpaRepository<Chambre, Long> {
    
    // Méthodes de base
    Optional<Chambre> findByNumero(String numero);
    List<Chambre> findByEtage(Integer etage);
    List<Chambre> findByType(TypeChambre type);
    List<Chambre> findByAccessibleTrue();
    List<Chambre> findByCapaciteGreaterThanEqual(Integer capaciteMin);
    
    // Chambres disponibles - CORRIGÉE
    @Query("SELECT c FROM Chambre c WHERE c.id NOT IN (SELECT r.chambre.id FROM Resident r WHERE r.statut = 'ACTIF' AND r.chambre IS NOT NULL)")
    List<Chambre> findChambresDisponibles();
    
    // Chambres occupées
    @Query("SELECT c FROM Chambre c WHERE c.id IN (SELECT r.chambre.id FROM Resident r WHERE r.statut = 'ACTIF' AND r.chambre IS NOT NULL)")
    List<Chambre> findByDisponibleFalse();
    
    // Occupation - CORRIGÉE
    @Query("SELECT c, (SELECT COUNT(r) FROM Resident r WHERE r.chambre.id = c.id AND r.statut = 'ACTIF') as occupation FROM Chambre c")
    List<Object[]> findOccupationChambres();
    
    // Disponibilités - CORRIGÉE
    @Query("SELECT COUNT(c) FROM Chambre c WHERE c.id NOT IN (SELECT r.chambre.id FROM Resident r WHERE r.statut = 'ACTIF' AND r.chambre IS NOT NULL)")
    long countChambresDisponibles();
    
    // Recherche par équipement - CORRIGÉE
    @Query("SELECT DISTINCT c FROM Chambre c JOIN c.equipements e WHERE LOWER(e.nom) LIKE LOWER(CONCAT('%', :nomEquipement, '%'))")
    List<Chambre> findByEquipementNom(@Param("nomEquipement") String nomEquipement);
    
    // Statistiques - CORRIGÉE
    @Query("SELECT c.type, COUNT(c), (SELECT COUNT(r) FROM Resident r WHERE r.chambre.id = c.id AND r.statut = 'ACTIF') FROM Chambre c GROUP BY c.type")
    List<Object[]> findStatistiquesParType();
    
    // Méthode utilitaire - CORRIGÉE
    @Query("SELECT COUNT(r) FROM Resident r WHERE r.chambre.id = :chambreId AND r.statut = 'ACTIF'")
    Long countResidentsActifsByChambreId(@Param("chambreId") Long chambreId);
    
     @Query("SELECT c FROM Chambre c WHERE c.id IN (SELECT r.chambre.id FROM Resident r WHERE r.statut = 'ACTIF' AND r.chambre IS NOT NULL) AND SIZE(c.residents) >= c.capacite")
    List<Chambre> findChambresPleines();
    
    // AJOUT : Chambres presque pleines (1 place restante)
    @Query("SELECT c FROM Chambre c WHERE c.id IN (SELECT r.chambre.id FROM Resident r WHERE r.statut = 'ACTIF' AND r.chambre IS NOT NULL) AND SIZE(c.residents) = c.capacite - 1")
    List<Chambre> findChambresPresquePleines();
}