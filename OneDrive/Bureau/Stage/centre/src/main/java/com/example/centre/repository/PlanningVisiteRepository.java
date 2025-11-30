/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.repository;

import com.example.centre.entity.PlanningVisite;
import com.example.centre.entity.TypeVisite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlanningVisiteRepository extends JpaRepository<PlanningVisite, Long> {
    List<PlanningVisite> findByResidentId(Long residentId);
    List<PlanningVisite> findBySoignantId(Long soignantId);
    List<PlanningVisite> findByType(TypeVisite type);
    List<PlanningVisite> findByEffectueeFalse();
    
    @Query("SELECT p FROM PlanningVisite p WHERE p.date = :date")
    List<PlanningVisite> findByDate(@Param("date") LocalDate date);
    
    @Query("SELECT p FROM PlanningVisite p WHERE p.date BETWEEN :start AND :end")
    List<PlanningVisite> findByDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);
    
    @Query("SELECT p FROM PlanningVisite p WHERE p.date < :date AND p.effectuee = false")
    List<PlanningVisite> findVisitesEnRetard(@Param("date") LocalDate date);
    
    @Query("SELECT p FROM PlanningVisite p WHERE p.resident.besoinInfirmier = true AND p.type = 'INFIRMIER' AND p.effectuee = false")
    List<PlanningVisite> findVisitesInfirmierPlanifiees();
    
    @Query("SELECT p FROM PlanningVisite p WHERE p.resident.besoinAideSoignant = true AND p.type = 'AIDE_SOIGNANT' AND p.effectuee = false")
    List<PlanningVisite> findVisitesAideSoignantPlanifiees();
}