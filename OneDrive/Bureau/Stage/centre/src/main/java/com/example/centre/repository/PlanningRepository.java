/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.centre.repository;

import com.example.centre.entity.Planning;
import com.example.centre.entity.TypeShift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlanningRepository extends JpaRepository<Planning, Long> {
    List<Planning> findByPersonnelId(Long personnelId);
    List<Planning> findByDate(LocalDate date);
    List<Planning> findByShift(TypeShift shift);
    List<Planning> findByValideFalse();
    
    @Query("SELECT p FROM Planning p WHERE p.date BETWEEN :start AND :end")
    List<Planning> findByDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);
    
    @Query("SELECT p FROM Planning p WHERE p.personnel.id = :personnelId AND p.date BETWEEN :start AND :end")
    List<Planning> findByPersonnelAndDateBetween(@Param("personnelId") Long personnelId, @Param("start") LocalDate start, @Param("end") LocalDate end);
    
    @Query("SELECT p FROM Planning p WHERE p.date = :date AND p.valide = true")
    List<Planning> findPlanningValideDuJour(@Param("date") LocalDate date);
}
