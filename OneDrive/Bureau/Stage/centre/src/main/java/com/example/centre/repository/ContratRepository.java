/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.centre.repository;

import com.example.centre.entity.Contrat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContratRepository extends JpaRepository<Contrat, Long> {
    Optional<Contrat> findByResidentId(Long residentId);
    List<Contrat> findByActifTrue();
    
    @Query("SELECT c FROM Contrat c WHERE c.dateDebut BETWEEN :start AND :end")
    List<Contrat> findContratsDebutBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);
    
    @Query("SELECT c FROM Contrat c WHERE c.dateFin BETWEEN :start AND :end")
    List<Contrat> findContratsExpirantBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);
    
    @Query("SELECT c FROM Contrat c WHERE c.dateFin IS NOT NULL AND c.dateFin < :date AND c.actif = true")
    List<Contrat> findContratsExpires(@Param("date") LocalDate date);
    
    @Query("SELECT c FROM Contrat c WHERE c.numeroContrat = :numero")
    Optional<Contrat> findByNumeroContrat(@Param("numero") String numero);
    
    @Query("SELECT COUNT(c) FROM Contrat c WHERE c.actif = true")
    Long countContratsActifs();
}