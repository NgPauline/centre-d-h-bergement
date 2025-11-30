/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.centre.repository;

import com.example.centre.entity.Activite;
import com.example.centre.entity.TypeActivite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActiviteRepository extends JpaRepository<Activite, Long> {
    List<Activite> findByType(TypeActivite type);
    List<Activite> findByResponsableId(Long responsableId);
    
    // CORRECTION : Ajouter les paramètres manquants
    @Query("SELECT COUNT(a) FROM Activite a WHERE a.dateHeure BETWEEN :startOfWeek AND :endOfWeek")
    long countActivitesThisWeek(@Param("startOfWeek") LocalDateTime startOfWeek, 
                               @Param("endOfWeek") LocalDateTime endOfWeek);
    
    @Query("SELECT COUNT(p) FROM ParticipationActivite p WHERE DATE(p.activite.dateHeure) = CURRENT_DATE")
    long countTotalParticipantsToday();

    @Query("SELECT COUNT(a) FROM Activite a WHERE DATE(a.dateHeure) = CURRENT_DATE")
    long countActivitesToday();
    
    @Query("SELECT a FROM Activite a WHERE a.dateHeure BETWEEN :start AND :end")
    List<Activite> findByDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT a FROM Activite a WHERE a.dateHeure >= :date")
    List<Activite> findFutureActivities(@Param("date") LocalDateTime date);
    
    @Query("SELECT a FROM Activite a WHERE a.lieu = :lieu")
    List<Activite> findByLieu(@Param("lieu") String lieu);
    
    @Query("SELECT a FROM Activite a WHERE SIZE(a.participations) < a.capaciteMax")
    List<Activite> findActivitiesWithAvailablePlaces();
    
    @Query("SELECT a FROM Activite a WHERE a.dateHeure < :now")
    List<Activite> findPastActivities(@Param("now") LocalDateTime now);
    
    // AJOUT : Méthode par défaut simplifiée
    default long countActivitesThisWeek() {
        LocalDateTime startOfWeek = LocalDateTime.now().minusDays(7);
        LocalDateTime endOfWeek = LocalDateTime.now();
        return countActivitesThisWeek(startOfWeek, endOfWeek);
    }
}