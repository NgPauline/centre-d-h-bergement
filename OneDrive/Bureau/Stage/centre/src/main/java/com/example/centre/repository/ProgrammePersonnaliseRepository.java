/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.centre.repository;

import com.example.centre.entity.ProgrammePersonnalise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProgrammePersonnaliseRepository extends JpaRepository<ProgrammePersonnalise, Long> {
    List<ProgrammePersonnalise> findByResidentId(Long residentId);
    List<ProgrammePersonnalise> findByResponsableId(Long responsableId);
    
    @Query("SELECT p FROM ProgrammePersonnalise p WHERE p.dateDebut <= :date AND (p.dateFin IS NULL OR p.dateFin >= :date)")
    List<ProgrammePersonnalise> findProgrammesEnCours(@Param("date") LocalDate date);
    
    @Query("SELECT p FROM ProgrammePersonnalise p WHERE p.dateFin IS NOT NULL AND p.dateFin < :date")
    List<ProgrammePersonnalise> findProgrammesTermines(@Param("date") LocalDate date);
    
    @Query("SELECT p FROM ProgrammePersonnalise p WHERE SIZE(p.activites) > 0")
    List<ProgrammePersonnalise> findProgrammesAvecActivites();
    
    @Query("SELECT p FROM ProgrammePersonnalise p WHERE LOWER(p.titre) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<ProgrammePersonnalise> findByTitreContaining(@Param("keyword") String keyword);
}
