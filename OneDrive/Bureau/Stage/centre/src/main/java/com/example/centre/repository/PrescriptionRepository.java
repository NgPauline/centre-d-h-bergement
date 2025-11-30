/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.repository;

import com.example.centre.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByDossierMedicalResidentId(Long residentId);
    List<Prescription> findByActiveTrue();
    
    @Query("SELECT p FROM Prescription p WHERE p.active = true AND (p.dateFin IS NULL OR p.dateFin >= :date)")
    List<Prescription> findPrescriptionsActives(@Param("date") LocalDate date);
    
    @Query("SELECT p FROM Prescription p WHERE p.dateFin IS NOT NULL AND p.dateFin < :date")
    List<Prescription> findPrescriptionsExpirees(@Param("date") LocalDate date);
    
    @Query("SELECT p FROM Prescription p WHERE p.medecinExterne LIKE %:medecin%")
    List<Prescription> findByMedecinContaining(@Param("medecin") String medecin);
}