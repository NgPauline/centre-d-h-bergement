/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.centre.repository;

import com.example.centre.entity.DossierMedical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DossierMedicalRepository extends JpaRepository<DossierMedical, Long> {
    Optional<DossierMedical> findByResidentId(Long residentId);
    Boolean existsByResidentId(Long residentId);
    
    @Query("SELECT dm FROM DossierMedical dm WHERE SIZE(dm.allergies) > 0")
    List<DossierMedical> findWithAllergies();
    
    @Query("SELECT dm FROM DossierMedical dm WHERE SIZE(dm.prescriptions) > 0")
    List<DossierMedical> findWithPrescriptions();
    
    @Query("SELECT dm FROM DossierMedical dm WHERE dm.derniereMAJ >= :date")
    List<DossierMedical> findModifiedAfter(@Param("date") java.time.LocalDateTime date);
}