/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.centre.repository;

import com.example.centre.entity.ParticipationActivite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationActiviteRepository extends JpaRepository<ParticipationActivite, Long> {
    List<ParticipationActivite> findByResidentId(Long residentId);
    List<ParticipationActivite> findByActiviteId(Long activiteId);
    List<ParticipationActivite> findByPresentTrue();
    
    @Query("SELECT p FROM ParticipationActivite p WHERE p.resident.id = :residentId AND p.activite.id = :activiteId")
    Optional<ParticipationActivite> findByResidentAndActivite(@Param("residentId") Long residentId, @Param("activiteId") Long activiteId);
    
    @Query("SELECT COUNT(p) FROM ParticipationActivite p WHERE p.activite.id = :activiteId AND p.present = true")
    Long countPresencesByActivite(@Param("activiteId") Long activiteId);
    
    @Query("SELECT p FROM ParticipationActivite p WHERE p.note IS NOT NULL AND p.note >= 7")
    List<ParticipationActivite> findParticipationsPositives();
}