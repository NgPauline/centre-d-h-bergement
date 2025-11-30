/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.centre.repository;

import com.example.centre.entity.RapportIncident;
import com.example.centre.entity.TypeIncident;
import com.example.centre.entity.NiveauGravite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RapportIncidentRepository extends JpaRepository<RapportIncident, Long> {
    List<RapportIncident> findByResidentId(Long residentId);
    List<RapportIncident> findByDeclarantId(Long declarantId);
    List<RapportIncident> findByType(TypeIncident type);
    List<RapportIncident> findByGravite(NiveauGravite gravite);
    List<RapportIncident> findByClotureFalse();
    
    @Query("SELECT r FROM RapportIncident r WHERE r.dateHeure BETWEEN :start AND :end")
    List<RapportIncident> findByDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT r FROM RapportIncident r WHERE r.familleInformee = false")
    List<RapportIncident> findIncidentsFamilleNonInformee();
    
    @Query("SELECT r FROM RapportIncident r WHERE r.gravite IN ('SEVERE', 'CRITIQUE')")
    List<RapportIncident> findIncidentsGraves();
    
    @Query("SELECT COUNT(r) FROM RapportIncident r WHERE r.dateHeure >= :date")
    Long countIncidentsRecents(@Param("date") LocalDateTime date);
}
