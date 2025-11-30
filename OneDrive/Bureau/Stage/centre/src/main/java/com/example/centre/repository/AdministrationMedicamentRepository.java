/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.centre.repository;

import com.example.centre.entity.AdministrationMedicament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdministrationMedicamentRepository extends JpaRepository<AdministrationMedicament, Long> {
    List<AdministrationMedicament> findByPrescriptionId(Long prescriptionId);
    List<AdministrationMedicament> findByInfirmierId(Long infirmierId);
    List<AdministrationMedicament> findByAdministreFalse();
    
    @Query("SELECT a FROM AdministrationMedicament a WHERE a.dateHeurePlanifiee BETWEEN :start AND :end")
    List<AdministrationMedicament> findByDatePlanifieeBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT a FROM AdministrationMedicament a WHERE a.dateHeurePlanifiee <= :date AND a.administre = false")
    List<AdministrationMedicament> findAdministrationsEnRetard(@Param("date") LocalDateTime date);
    
    @Query("SELECT a FROM AdministrationMedicament a WHERE a.prescription.resident.id = :residentId AND a.administre = false")
    List<AdministrationMedicament> findAdministrationsEnAttenteParResident(@Param("residentId") Long residentId);
    
    @Query("SELECT a FROM AdministrationMedicament a WHERE DATE(a.dateHeurePlanifiee) = CURRENT_DATE AND a.administre = false")
    List<AdministrationMedicament> findAdministrationsDuJour();
    
    @Query("SELECT COUNT(a) FROM AdministrationMedicament a WHERE a.administre = true AND a.dateHeureAdministration BETWEEN :start AND :end")
    Long countAdministrationsEffectuees(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}