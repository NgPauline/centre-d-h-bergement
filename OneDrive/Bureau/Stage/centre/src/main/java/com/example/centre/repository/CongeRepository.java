/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.centre.repository;

import com.example.centre.entity.Conge;
import com.example.centre.entity.TypeConge;
import com.example.centre.entity.StatutDemande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CongeRepository extends JpaRepository<Conge, Long> {
    List<Conge> findByPersonnelId(Long personnelId);
    List<Conge> findByType(TypeConge type);
    List<Conge> findByStatut(StatutDemande statut);
    
    @Query("SELECT c FROM Conge c WHERE c.dateDebut BETWEEN :start AND :end OR c.dateFin BETWEEN :start AND :end")
    List<Conge> findCongesChevauchantPeriode(@Param("start") LocalDate start, @Param("end") LocalDate end);
    
    @Query("SELECT c FROM Conge c WHERE c.personnel.id = :personnelId AND ((c.dateDebut BETWEEN :start AND :end) OR (c.dateFin BETWEEN :start AND :end))")
    List<Conge> findCongesPersonnelPeriode(@Param("personnelId") Long personnelId, @Param("start") LocalDate start, @Param("end") LocalDate end);
    
    @Query("SELECT c FROM Conge c WHERE c.statut = 'EN_ATTENTE'")
    List<Conge> findDemandesEnAttente();
    
    @Query("SELECT c FROM Conge c WHERE c.dateDebut <= :date AND c.dateFin >= :date AND c.statut = 'APPROUVEE'")
    List<Conge> findPersonnelEnConge(@Param("date") LocalDate date);
    
    @Query("SELECT COUNT(c) FROM Conge c WHERE c.statut = 'APPROUVEE' AND c.dateDebut <= :date AND c.dateFin >= :date")
    Long countPersonnelEnConge(@Param("date") LocalDate date);
}