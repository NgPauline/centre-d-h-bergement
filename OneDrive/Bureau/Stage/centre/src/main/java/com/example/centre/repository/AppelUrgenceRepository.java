/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.centre.repository;

import com.example.centre.entity.AppelUrgence;
import com.example.centre.entity.TypeUrgence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppelUrgenceRepository extends JpaRepository<AppelUrgence, Long> {
    List<AppelUrgence> findByResidentId(Long residentId);
    List<AppelUrgence> findByAppelantId(Long appelantId);
    List<AppelUrgence> findByType(TypeUrgence type);
    List<AppelUrgence> findByAmbulanceAppeleeTrue();
    
    @Query("SELECT a FROM AppelUrgence a WHERE a.dateHeure BETWEEN :start AND :end")
    List<AppelUrgence> findByDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    // CORRECTION : Utiliser le bon nom d'attribut
    @Query("SELECT a FROM AppelUrgence a WHERE a.familleNotifiee = false")
    List<AppelUrgence> findAppelsFamilleNonNotifiee();
    
    // CORRECTION : Utiliser le bon nom d'attribut
    @Query("SELECT a FROM AppelUrgence a WHERE a.cloture = false")
    List<AppelUrgence> findAppelsNonClotures();
    
    @Query("SELECT COUNT(a) FROM AppelUrgence a WHERE a.dateHeure >= :date")
    Long countAppelsRecents(@Param("date") LocalDateTime date);
}